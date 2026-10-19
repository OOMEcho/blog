package com.blog.modules.photography.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.common.constant.PhotographyConstants;
import com.blog.common.file.FileAccessType;
import com.blog.common.file.ByteArrayMultipartFile;
import com.blog.common.file.FileStorageServiceFactory;
import com.blog.common.file.service.FileStorageService;
import com.blog.modules.file.domain.entity.FileMetadata;
import com.blog.modules.file.mapper.FileMetadataMapper;
import com.blog.common.domain.vo.PageVO;
import com.blog.common.exception.BusinessException;
import com.blog.common.result.ResultCodeEnum;
import com.blog.common.file.StoragePlatform;
import com.blog.modules.photography.domain.dto.PhotoAlbumContentDTO;
import com.blog.modules.photography.domain.dto.PhotoAlbumPhotoDTO;
import com.blog.modules.photography.domain.dto.PhotoAlbumQueryDTO;
import com.blog.modules.photography.domain.dto.PhotoAlbumSaveDTO;
import com.blog.modules.photography.domain.dto.PhotoImageResultDTO;
import com.blog.modules.photography.domain.dto.PhotoQueryDTO;
import com.blog.modules.photography.domain.entity.Photo;
import com.blog.modules.photography.domain.entity.PhotoAlbum;
import com.blog.modules.photography.domain.vo.PhotoAlbumVO;
import com.blog.modules.photography.domain.vo.PhotoVO;
import com.blog.modules.photography.mapper.PhotoAlbumMapper;
import com.blog.modules.photography.mapper.PhotoMapper;
import com.blog.modules.photography.service.PhotoImageService;
import com.blog.modules.photography.service.PhotographyConvert;
import com.blog.modules.photography.service.PhotographyService;
import com.blog.utils.PageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 摄影相册业务实现层
 *
 * @author xuesong.lei
 * @since 2026-10-19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PhotographyServiceImpl implements PhotographyService {

    private final PhotoAlbumMapper photoAlbumMapper;

    private final PhotoMapper photoMapper;

    private final PhotographyConvert photographyConvert;

    private final ObjectMapper objectMapper;

    private final TransactionTemplate transactionTemplate;

    private final FileStorageServiceFactory fileStorageServiceFactory;

    private final FileMetadataMapper fileMetadataMapper;

    private final PhotoImageService photoImageService;

    private final Semaphore processing = new Semaphore(1);

    @Override
    public PageVO<PhotoAlbumVO> pageList(PhotoAlbumQueryDTO dto) {
        return queryPageList(dto, false);
    }

    @Override
    public PageVO<PhotoAlbumVO> publishedPageList(PhotoAlbumQueryDTO dto) {
        return queryPageList(dto, true);
    }

    @Override
    public PhotoAlbumVO detail(Long id) {
        PhotoAlbum album = getAlbum(id, false);
        PhotoAlbumContentDTO content = readContent(album, false);
        PhotoAlbumVO vo = toAlbumVO(album, content, false);
        vo.setContent(content);
        vo.setPhotos(toPhotoVOList(id, content.getPhotos()));
        return vo;
    }

    @Override
    public PhotoAlbumVO publishedDetail(Long id) {
        PhotoAlbum album = getAlbum(id, false);
        return toAlbumVO(album, readContent(album, true), true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PhotoAlbumVO add(PhotoAlbumContentDTO dto) {
        if (!dto.getPhotos().isEmpty() || dto.getCoverPhotoId() != null) {
            throw new BusinessException("请先创建相册再上传照片");
        }
        validateContent(null, dto, false);

        PhotoAlbum album = new PhotoAlbum();
        album.setStatus("DRAFT");
        album.setRevision(1);
        album.setDraftJson(writeContent(dto));
        album.setUpdatedAt(LocalDateTime.now());
        photoAlbumMapper.insert(album);
        return detail(album.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PhotoAlbumVO update(PhotoAlbumSaveDTO dto) {
        PhotoAlbum album = getAlbum(dto.getId(), true);
        checkRevision(album, dto.getRevision());
        validateContent(dto.getId(), dto.getContent(), false);
        album.setDraftJson(writeContent(dto.getContent()));
        updateAlbum(album);
        return detail(dto.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PhotoAlbumVO publish(Long id, Integer revision) {
        PhotoAlbum album = getAlbum(id, true);
        checkRevision(album, revision);
        validateContent(id, readContent(album, false), true);
        album.setPublishedJson(album.getDraftJson());
        album.setStatus("PUBLISHED");
        updateAlbum(album);
        return detail(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PhotoAlbumVO unpublish(Long id, Integer revision) {
        PhotoAlbum album = getAlbum(id, true);
        checkRevision(album, revision);
        album.setStatus("OFFLINE");
        updateAlbum(album);
        return detail(id);
    }

    @Override
    public PageVO<PhotoVO> publishedPhotos(Long id, PhotoQueryDTO dto) {
        PhotoAlbumContentDTO content = readContent(getAlbum(id, false), true);
        int pageSize = dto.getPageSize();
        int pageNum = dto.getPageNum();
        int start = (int) Math.min(content.getPhotos().size(), ((long) pageNum - 1) * pageSize);
        int end = Math.min(content.getPhotos().size(), start + pageSize);
        List<PhotoVO> records = toPhotoVOList(id, content.getPhotos().subList(start, end));
        return new PageVO<>(records, (long) content.getPhotos().size(), (long) pageNum, (long) pageSize);
    }

    @Override
    public PhotoAlbumVO upload(Long id, MultipartFile file) {
        getAlbum(id, false);
        validateUpload(file);
        if (!processing.tryAcquire()) {
            throw new BusinessException("正在处理其他照片，请稍后重试");
        }

        Photo staged = null;
        AtomicBoolean committed = new AtomicBoolean(false);
        try {
            byte[] bytes = file.getBytes();
            String digest = hex(MessageDigest.getInstance("SHA-256").digest(bytes));
            Photo existing = findDuplicate(id, digest);
            if (existing != null) {
                return transactionTemplate.execute(status -> appendPhoto(id, existing));
            }

            PhotoImageResultDTO result = photoImageService.process(bytes);
            staged = result.getPhoto();
            staged.setAlbumId(id);
            staged.setSha256(digest);
            staged.setOriginalName(file.getOriginalFilename());
            staged.setFileSize((long) bytes.length);
            FileStorageService storageService = fileStorageServiceFactory.getFileStorageService();
            staged.setOriginalFileId(storageService.upload(file, PhotographyConstants.DIRECTORY, FileAccessType.PRIVATE).getId());
            staged.setThumbnailFileId(storageService.upload(
                    new ByteArrayMultipartFile("thumbnail.jpg", "image/jpeg", result.getThumbnail()),
                    PhotographyConstants.DIRECTORY, FileAccessType.PRIVATE).getId());
            staged.setDisplayFileId(storageService.upload(
                    new ByteArrayMultipartFile("display.jpg", "image/jpeg", result.getDisplay()),
                    PhotographyConstants.DIRECTORY, FileAccessType.PRIVATE).getId());

            Photo photo = staged;
            // 图片处理和文件存储不占用数据库事务，入库及追加草稿在同一事务内完成。
            return transactionTemplate.execute(status -> {
                getAlbum(id, true);
                Photo duplicate = findDuplicate(id, digest);
                if (duplicate != null) {
                    return appendPhoto(id, duplicate);
                }
                photoMapper.insert(photo);
                PhotoAlbumVO vo = appendPhoto(id, photo);
                committed.set(true);
                return vo;
            });
        } catch (BusinessException e) {
            committed.set(false);
            throw e;
        } catch (Exception e) {
            committed.set(false);
            log.error("摄影照片上传失败，相册ID={}", id, e);
            throw new BusinessException("照片上传失败，请重试");
        } finally {
            if (staged != null && !committed.get()) {
                cleanup(staged);
            }
            processing.release();
        }
    }

    @Override
    public void preview(Long id, Long photoId, String variant, boolean published, HttpServletResponse response) {
        FileMetadata metadata = resolvePhotoFile(id, photoId, variant, published);
        FileStorageService storageService = fileStorageServiceFactory.getFileStorageService(
                StoragePlatform.valueOf(metadata.getPlatform()));
        try (InputStream input = storageService.download(metadata.getFilePath())) {
            response.setContentType("image/jpeg");
            response.setHeader("Cache-Control", "private, no-store");
            response.setHeader("X-Content-Type-Options", "nosniff");
            if ("original".equals(variant)) {
                response.setHeader("Content-Disposition", "attachment; filename=\"photo-" + photoId + ".jpg\"");
            }
            response.setContentLengthLong(metadata.getFileSize());
            byte[] buffer = new byte[8192];
            int length;
            while ((length = input.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, length);
            }
            response.getOutputStream().flush();
        } catch (IOException e) {
            log.error("摄影图片输出失败，照片ID={}", photoId, e);
            throw new BusinessException("照片读取失败，请重试");
        }
    }

    /**
     * 按草稿或发布快照查询分页列表
     */
    private PageVO<PhotoAlbumVO> queryPageList(PhotoAlbumQueryDTO dto, boolean published) {
        LambdaQueryWrapper<PhotoAlbum> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(published, PhotoAlbum::getStatus, "PUBLISHED");
        if (!published) {
            queryWrapper.orderByDesc(PhotoAlbum::getUpdatedAt);
        }
        // 公开列表按创建顺序排列，保存草稿不改变访客看到的相册顺序。
        queryWrapper.orderByDesc(PhotoAlbum::getId);
        return PageUtils.setPage(dto.getPageNum(), dto.getPageSize())
                .pagingAndConvert(photoAlbumMapper, queryWrapper,
                        album -> toAlbumVO(album, readContent(album, published), published));
    }

    /**
     * 读取相册，可按需锁定记录以串行处理修改
     */
    private PhotoAlbum getAlbum(Long id, boolean locked) {
        PhotoAlbum album = locked ? photoAlbumMapper.lock(id) : photoAlbumMapper.selectById(id);
        if (album == null) {
            throw notFound();
        }
        return album;
    }

    /**
     * 读取草稿或发布快照，并校验发布状态
     */
    private PhotoAlbumContentDTO readContent(PhotoAlbum album, boolean published) {
        String value = published ? album.getPublishedJson() : album.getDraftJson();
        if (published && (!"PUBLISHED".equals(album.getStatus()) || value == null)) {
            throw notFound();
        }
        try {
            return objectMapper.readValue(value, PhotoAlbumContentDTO.class);
        } catch (Exception e) {
            log.error("相册内容读取失败，相册ID={}", album.getId(), e);
            throw new BusinessException("相册数据读取失败");
        }
    }

    private String writeContent(PhotoAlbumContentDTO content) {
        try {
            return objectMapper.writeValueAsString(content);
        } catch (Exception e) {
            log.error("相册内容序列化失败", e);
            throw new BusinessException("相册保存失败");
        }
    }

    private PhotoAlbumVO toAlbumVO(PhotoAlbum album, PhotoAlbumContentDTO content, boolean published) {
        PhotoAlbumVO vo = photographyConvert.toPhotoAlbumVO(content);
        vo.setId(album.getId());
        if (!published) {
            vo.setStatus(album.getStatus());
            vo.setRevision(album.getRevision());
            vo.setUpdatedAt(album.getUpdatedAt());
            vo.setHasUnpublishedChanges(!Objects.equals(album.getDraftJson(), album.getPublishedJson()));
        }
        return vo;
    }

    /**
     * 按快照顺序组装照片VO，不暴露文件存储路径
     */
    private List<PhotoVO> toPhotoVOList(Long albumId, List<PhotoAlbumPhotoDTO> entries) {
        if (entries.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> ids = entries.stream().map(PhotoAlbumPhotoDTO::getPhotoId).collect(Collectors.toList());
        LambdaQueryWrapper<Photo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Photo::getAlbumId, albumId).in(Photo::getId, ids);
        Map<Long, Photo> index = photoMapper.selectList(queryWrapper).stream()
                .collect(Collectors.toMap(Photo::getId, photo -> photo));
        List<PhotoVO> result = new ArrayList<>();
        for (PhotoAlbumPhotoDTO entry : entries) {
            Photo photo = index.get(entry.getPhotoId());
            if (photo == null) {
                throw new BusinessException("照片记录缺失，请联系管理员");
            }
            PhotoVO vo = photographyConvert.toPhotoVO(photo);
            vo.setCaption(entry.getCaption());
            if (entry.getTakenAt() != null) {
                vo.setTakenAt(entry.getTakenAt());
            }
            result.add(vo);
        }
        return result;
    }

    private void checkRevision(PhotoAlbum album, Integer revision) {
        if (!Objects.equals(album.getRevision(), revision)) {
            throw new BusinessException("相册已更新，请重新加载后再保存");
        }
    }

    /**
     * 校验照片归属、封面、数量和日期，发布时必须已有照片及封面
     */
    private void validateContent(Long albumId, PhotoAlbumContentDTO content, boolean publishing) {
        Set<Long> ids = new HashSet<>();
        for (PhotoAlbumPhotoDTO entry : content.getPhotos()) {
            if (entry == null || entry.getPhotoId() == null || !ids.add(entry.getPhotoId())) {
                throw new BusinessException("相册包含重复或无效照片");
            }
        }
        if (!ids.isEmpty()) {
            LambdaQueryWrapper<Photo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Photo::getAlbumId, albumId).in(Photo::getId, ids);
            if (photoMapper.selectCount(queryWrapper) != ids.size()) {
                throw new BusinessException("相册中包含不属于本相册的照片");
            }
        }
        if (content.getCoverPhotoId() != null && !ids.contains(content.getCoverPhotoId())) {
            throw new BusinessException("封面必须是相册内的照片");
        }
        if (publishing && (ids.isEmpty() || content.getCoverPhotoId() == null)) {
            throw new BusinessException("请先上传照片并选择封面");
        }
        if (content.getDate() != null && !content.getDate().isEmpty()) {
            try {
                LocalDate.parse(content.getDate());
            } catch (Exception e) {
                throw new BusinessException("相册日期无效");
            }
        }
    }

    private void updateAlbum(PhotoAlbum album) {
        album.setRevision(album.getRevision() + 1);
        album.setUpdatedAt(LocalDateTime.now());
        photoAlbumMapper.updateById(album);
    }

    private void validateUpload(MultipartFile file) {
        String name = file.getOriginalFilename();
        if (name == null || name.length() > 255 || name.contains("/") || name.contains("\\")
                || !name.toLowerCase(Locale.ROOT).matches(".*\\.jpe?g")) {
            throw new BusinessException("请选择文件名有效的 JPEG 照片");
        }
        if (file.isEmpty() || file.getSize() > PhotographyConstants.MAX_FILE_SIZE) {
            throw new BusinessException("JPEG 最大支持 50MB");
        }
    }

    private Photo findDuplicate(Long albumId, String digest) {
        LambdaQueryWrapper<Photo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Photo::getAlbumId, albumId).eq(Photo::getSha256, digest);
        return photoMapper.selectOne(queryWrapper);
    }

    private PhotoAlbumVO appendPhoto(Long id, Photo photo) {
        PhotoAlbum album = getAlbum(id, true);
        PhotoAlbumContentDTO content = readContent(album, false);
        if (content.getPhotos().stream().noneMatch(entry -> entry.getPhotoId().equals(photo.getId()))) {
            if (content.getPhotos().size() >= 500) {
                throw new BusinessException("每个相册最多支持 500 张照片");
            }
            PhotoAlbumPhotoDTO entry = new PhotoAlbumPhotoDTO();
            entry.setPhotoId(photo.getId());
            content.getPhotos().add(entry);
            if (content.getCoverPhotoId() == null) {
                content.setCoverPhotoId(photo.getId());
            }
            album.setDraftJson(writeContent(content));
            updateAlbum(album);
        }
        return detail(id);
    }

    private void cleanup(Photo photo) {
        List<Long> fileIds = Arrays.asList(photo.getOriginalFileId(), photo.getThumbnailFileId(), photo.getDisplayFileId());
        for (Long fileId : fileIds) {
            if (fileId == null) {
                continue;
            }
            try {
                FileMetadata metadata = getPhotoFile(fileId);
                fileStorageServiceFactory.getFileStorageService(StoragePlatform.valueOf(metadata.getPlatform()))
                        .delete(metadata.getFilePath());
                fileMetadataMapper.deleteById(fileId);
            } catch (Exception e) {
                log.warn("摄影临时文件清理失败，文件ID={}", fileId, e);
            }
        }
    }

    private String hex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte value : bytes) {
            result.append(String.format("%02x", value));
        }
        return result.toString();
    }

    private FileMetadata resolvePhotoFile(Long albumId, Long photoId, String variant, boolean published) {
        PhotoAlbumContentDTO content = readContent(getAlbum(albumId, false), published);
        if (content.getPhotos().stream().noneMatch(entry -> Objects.equals(entry.getPhotoId(), photoId))) {
            throw notFound();
        }
        Photo photo = photoMapper.selectById(photoId);
        if (photo == null || !albumId.equals(photo.getAlbumId())) {
            throw notFound();
        }
        Long fileId;
        switch (variant) {
            case "thumbnail":
                fileId = photo.getThumbnailFileId();
                break;
            case "display":
                fileId = photo.getDisplayFileId();
                break;
            case "original":
                fileId = photo.getOriginalFileId();
                break;
            default:
                throw notFound();
        }
        return getPhotoFile(fileId);
    }

    /**
     * 根据文件引用读取元数据，校验文件为私有访问类型
     */
    private FileMetadata getPhotoFile(Long fileId) {
        FileMetadata metadata = fileId == null ? null : fileMetadataMapper.selectById(fileId);
        if (metadata == null || metadata.getAccessType() != FileAccessType.PRIVATE) {
            throw notFound();
        }
        return metadata;
    }

    private BusinessException notFound() {
        return new BusinessException(ResultCodeEnum.NOT_FOUND);
    }
}
