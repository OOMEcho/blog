package com.blog.modules.photography.service.impl;

import com.blog.common.constant.PhotographyConstants;
import com.blog.common.exception.BusinessException;
import com.blog.modules.photography.domain.dto.PhotoImageResultDTO;
import com.blog.modules.photography.domain.entity.Photo;
import com.blog.modules.photography.service.PhotoImageService;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * 摄影照片处理实现层
 *
 * @author xuesong.lei
 * @since 2026-10-19
 */
@Slf4j
@Service
public class PhotoImageServiceImpl implements PhotoImageService {

    @Override
    public PhotoImageResultDTO process(byte[] bytes) {
        if (bytes.length == 0 || bytes.length > PhotographyConstants.MAX_FILE_SIZE) {
            throw new BusinessException("JPEG 最大支持 50MB");
        }
        if (bytes.length < 4 || (bytes[0] & 255) != 255 || (bytes[1] & 255) != 216) {
            throw new BusinessException("只支持有效的 JPEG 照片");
        }
        try (ImageInputStream input = ImageIO.createImageInputStream(new ByteArrayInputStream(bytes))) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
            if (!readers.hasNext()) {
                throw new BusinessException("无法读取照片");
            }
            ImageReader reader = readers.next();
            try {
                reader.setInput(input, true, true);
                if (!"JPEG".equalsIgnoreCase(reader.getFormatName())) {
                    throw new BusinessException("只支持 JPEG 照片");
                }
                int width = reader.getWidth(0);
                int height = reader.getHeight(0);
                if (width <= 0 || height <= 0 || width > 30000 || height > 30000
                        || (long) width * height > PhotographyConstants.MAX_PIXELS) {
                    throw new BusinessException("照片最大支持 1 亿像素，单边不超过 30000 像素");
                }

                PhotoImageResultDTO result = new PhotoImageResultDTO();
                int orientation = readMetadata(bytes, result.getPhoto());
                boolean swap = orientation >= 5 && orientation <= 8;
                result.getPhoto().setWidth(swap ? height : width);
                result.getPhoto().setHeight(swap ? width : height);
                ImageReadParam param = reader.getDefaultReadParam();
                int sample = Math.max(1, Math.max(width, height) / 2400);
                param.setSourceSubsampling(sample, sample, 0, 0);
                BufferedImage image = reader.read(0, param);
                BufferedImage rgb = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
                // 移除元数据前，将解码后的照片转换为sRGB色彩空间。
                new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_sRGB), null).filter(image, rgb);
                image.flush();
                BufferedImage upright = orient(rgb, orientation);
                result.setDisplay(encode(upright, 2400, .90f));
                result.setThumbnail(encode(upright, 800, .86f));
                upright.flush();
                if (upright != rgb) {
                    rgb.flush();
                }
                return result;
            } finally {
                reader.dispose();
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("摄影照片解码失败", e);
            throw new BusinessException("照片解码失败，请检查 JPEG 是否完整或重新导出");
        }
    }

    /**
     * 提取可用的拍摄信息及照片方向，缺失EXIF时使用正常方向
     */
    private int readMetadata(byte[] bytes, Photo photo) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(bytes));
            ExifIFD0Directory base = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            ExifSubIFDDirectory exif = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            if (base != null) {
                photo.setCamera(base.getString(ExifIFD0Directory.TAG_MODEL));
            }
            if (exif != null) {
                photo.setLens(exif.getString(ExifSubIFDDirectory.TAG_LENS_MODEL));
                photo.setAperture(exif.getDescription(ExifSubIFDDirectory.TAG_FNUMBER));
                photo.setShutter(exif.getDescription(ExifSubIFDDirectory.TAG_EXPOSURE_TIME));
                photo.setIso(exif.getString(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT));
                String date = exif.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                if (date != null && date.matches("\\d{4}:\\d{2}:\\d{2} .*")) {
                    photo.setTakenAt(date.substring(0, 10).replace(':', '-') + date.substring(10));
                }
            }
            Integer orientation = base == null ? null : base.getInteger(ExifIFD0Directory.TAG_ORIENTATION);
            return orientation != null && orientation >= 1 && orientation <= 8 ? orientation : 1;
        } catch (Exception e) {
            // 缺失或损坏的EXIF不影响有效JPEG的上传。
            log.debug("摄影照片EXIF读取失败，使用默认方向", e);
            return 1;
        }
    }

    /**
     * 应用EXIF方向变换，包含旋转及镜像方向
     */
    static BufferedImage orient(BufferedImage source, int orientation) {
        if (orientation <= 1 || orientation > 8) {
            return source;
        }
        int width = source.getWidth();
        int height = source.getHeight();
        boolean swap = orientation >= 5;
        BufferedImage result = new BufferedImage(swap ? height : width, swap ? width : height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int dx = x;
                int dy = y;
                switch (orientation) {
                    case 2:
                        dx = width - 1 - x;
                        break;
                    case 3:
                        dx = width - 1 - x;
                        dy = height - 1 - y;
                        break;
                    case 4:
                        dy = height - 1 - y;
                        break;
                    case 5:
                        dx = y;
                        dy = x;
                        break;
                    case 6:
                        dx = height - 1 - y;
                        dy = x;
                        break;
                    case 7:
                        dx = height - 1 - y;
                        dy = width - 1 - x;
                        break;
                    case 8:
                        dx = y;
                        dy = width - 1 - x;
                        break;
                    default:
                        break;
                }
                result.setRGB(dx, dy, source.getRGB(x, y));
            }
        }
        return result;
    }

    /**
     * 按长边生成JPEG，不放大原图且不复制EXIF和GPS信息
     */
    private byte[] encode(BufferedImage source, int edge, float quality) throws IOException {
        double ratio = Math.min(1d, edge / (double) Math.max(source.getWidth(), source.getHeight()));
        int width = Math.max(1, (int) Math.round(source.getWidth() * ratio));
        int height = Math.max(1, (int) Math.round(source.getHeight() * ratio));
        BufferedImage target = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = target.createGraphics();
        try {
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.drawImage(source, 0, 0, width, height, null);
        } finally {
            graphics.dispose();
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ImageWriter writer = ImageIO.getImageWritersByFormatName("JPEG").next();
        try (ImageOutputStream output = ImageIO.createImageOutputStream(bytes)) {
            writer.setOutput(output);
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);
            writer.write(null, new IIOImage(target, null, null), param);
            output.flush();
        } finally {
            writer.dispose();
            target.flush();
        }
        return bytes.toByteArray();
    }
}
