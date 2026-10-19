package com.blog.modules.photography.service.impl;

import com.blog.modules.photography.domain.dto.PhotoImageResultDTO;

import com.blog.common.exception.BusinessException;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.exif.GpsDirectory;
import org.junit.jupiter.api.Test;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.*;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 摄影照片处理组件测试
 *
 * @author xuesong.lei
 * @since 2026-10-19
 */
class PhotoImageServiceImplTest {
    private final PhotoImageServiceImpl processor = new PhotoImageServiceImpl();

    byte[] jpeg(int w, int h) throws Exception {
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setPaint(new GradientPaint(0, 0, Color.BLUE, w, h, new Color(255, 150, 70)));
        g.fillRect(0, 0, w, h); g.dispose();
        ByteArrayOutputStream out = new ByteArrayOutputStream(); ImageIO.write(image, "JPEG", out); return out.toByteArray();
    }
    void entry(ByteBuffer b, int tag, int type, int count, int value) {
        b.putShort((short)tag).putShort((short)type).putInt(count).putInt(value);
    }
    byte[] withExif(byte[] jpeg, int orientation) throws Exception {
        ByteBuffer tiff = ByteBuffer.allocate(256).order(ByteOrder.LITTLE_ENDIAN);
        tiff.put((byte)'I').put((byte)'I').putShort((short)42).putInt(8);
        tiff.putShort((short)4);
        entry(tiff, 0x0110, 2, 3, 'Z' | ('9' << 8));
        entry(tiff, 0x0112, 3, 1, orientation);
        entry(tiff, 0x8769, 4, 1, 62);
        entry(tiff, 0x8825, 4, 1, 216);
        tiff.putInt(0);
        tiff.position(62); tiff.putShort((short)6);
        entry(tiff, 0x829A, 5, 1, 208);
        entry(tiff, 0x829D, 5, 1, 200);
        entry(tiff, 0x8827, 3, 1, 200);
        entry(tiff, 0x9003, 2, 20, 170);
        entry(tiff, 0xA434, 2, 5, 190);
        entry(tiff, 0xA002, 4, 1, 120);
        tiff.putInt(0);
        tiff.position(170); tiff.put("2026:10:19 08:30:00\0".getBytes(StandardCharsets.US_ASCII));
        tiff.position(190); tiff.put("50mm\0".getBytes(StandardCharsets.US_ASCII));
        tiff.position(200); tiff.putInt(28).putInt(10).putInt(1).putInt(125);
        tiff.position(216); tiff.putShort((short)1); entry(tiff, 1, 2, 2, 'N'); tiff.putInt(0);
        ByteArrayOutputStream result = new ByteArrayOutputStream(); DataOutputStream out = new DataOutputStream(result);
        out.write(jpeg, 0, 2); out.writeShort(0xffe1); out.writeShort(2 + 6 + 256);
        out.write(new byte[]{'E','x','i','f',0,0}); out.write(tiff.array()); out.write(jpeg, 2, jpeg.length - 2);
        return result.toByteArray();
    }

    @Test void allExifOrientationsAndCameraMetadataAreHandledAndGpsIsRemoved() throws Exception {
        for (int orientation = 1; orientation <= 8; orientation++) {
            byte[] source = withExif(jpeg(120, 80), orientation);
            assertNotNull(ImageMetadataReader.readMetadata(new ByteArrayInputStream(source)).getFirstDirectoryOfType(GpsDirectory.class));
            PhotoImageResultDTO result = processor.process(source);
            assertEquals(orientation >= 5 ? 80 : 120, result.getPhoto().getWidth());
            assertEquals(orientation >= 5 ? 120 : 80, result.getPhoto().getHeight());
            BufferedImage output = ImageIO.read(new ByteArrayInputStream(result.getDisplay()));
            assertEquals(result.getPhoto().getWidth().intValue(), output.getWidth());
            assertEquals(result.getPhoto().getHeight().intValue(), output.getHeight());
            assertEquals("Z9", result.getPhoto().getCamera()); assertEquals("50mm", result.getPhoto().getLens());
            assertEquals("2026-10-19 08:30:00", result.getPhoto().getTakenAt());
            assertNotNull(result.getPhoto().getAperture()); assertNotNull(result.getPhoto().getShutter()); assertEquals("200", result.getPhoto().getIso());
            assertNull(ImageMetadataReader.readMetadata(new ByteArrayInputStream(result.getDisplay())).getFirstDirectoryOfType(GpsDirectory.class));
            assertNull(ImageMetadataReader.readMetadata(new ByteArrayInputStream(result.getThumbnail())).getFirstDirectoryOfType(GpsDirectory.class));
        }
    }

    @Test void orientationMovesPixelsCorrectlyIncludingMirroredImages() {
        BufferedImage src = new BufferedImage(3, 2, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 2; y++) for (int x = 0; x < 3; x++) src.setRGB(x, y, 1 + y * 3 + x);
        int[][] expected = {{1,2,3,4,5,6}, {3,2,1,6,5,4}, {6,5,4,3,2,1}, {4,5,6,1,2,3},
                {1,4,2,5,3,6}, {4,1,5,2,6,3}, {6,3,5,2,4,1}, {3,6,2,5,1,4}};
        for (int orientation = 1; orientation <= 8; orientation++) {
            BufferedImage out = PhotoImageServiceImpl.orient(src, orientation); int i = 0;
            for (int y = 0; y < out.getHeight(); y++) for (int x = 0; x < out.getWidth(); x++)
                assertEquals(expected[orientation - 1][i++], out.getRGB(x,y) & 0xffffff);
        }
    }

    @Test void imagesResizeWithoutUpscalingAndMissingExifIsAccepted() throws Exception {
        PhotoImageResultDTO large = processor.process(jpeg(3200, 2000));
        BufferedImage thumb = ImageIO.read(new ByteArrayInputStream(large.getThumbnail()));
        BufferedImage display = ImageIO.read(new ByteArrayInputStream(large.getDisplay()));
        assertEquals(800, thumb.getWidth()); assertEquals(500, thumb.getHeight());
        assertEquals(2400, display.getWidth()); assertEquals(1500, display.getHeight());
        PhotoImageResultDTO small = processor.process(jpeg(120, 80));
        assertEquals(120, ImageIO.read(new ByteArrayInputStream(small.getThumbnail())).getWidth());
        assertNull(small.getPhoto().getCamera()); assertNull(small.getPhoto().getTakenAt());
    }

    @Test void rejectsFakeJpegTruncationAndOversizedPixelHeaders() throws Exception {
        assertThrows(BusinessException.class, () -> processor.process(new byte[]{(byte)0xff, (byte)0xd8, 0, 0}));
        ByteArrayOutputStream png = new ByteArrayOutputStream(); ImageIO.write(new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB), "PNG", png);
        assertThrows(BusinessException.class, () -> processor.process(png.toByteArray()));
        byte[] jpeg = jpeg(30, 20);
        // Patch the JPEG SOF dimensions; decoding must be rejected before allocation.
        for (int i = 2; i < jpeg.length - 9; i++) if ((jpeg[i] & 255) == 255 && ((jpeg[i+1] & 255) == 192 || (jpeg[i+1] & 255) == 194)) {
            jpeg[i+5] = 0x75; jpeg[i+6] = 0x30; jpeg[i+7] = 0x75; jpeg[i+8] = 0x30; break;
        }
        assertTrue(assertThrows(BusinessException.class, () -> processor.process(jpeg)).getMessage().contains("像素"));
    }
}
