package com.blog.common.file.service;

import com.blog.common.file.FileAccessType;
import com.blog.modules.file.domain.entity.FileMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.Duration;

/**
 * 文件存储服务接口
 *
 * @author xuesong.lei
 * @since 2025-08-27
 */
public interface FileStorageService {

    /**
     * 上传公开文件，保持原有调用方式
     */
    FileMetadata upload(MultipartFile file, String directory);

    /**
     * 按指定访问类型上传文件，元数据入库时即确定访问属性
     */
    FileMetadata upload(MultipartFile file, String directory, FileAccessType accessType);

    /**
     * 下载文件
     */
    InputStream download(String filePath);

    /**
     * 删除文件
     */
    void delete(String filePath);

    /**
     * 检查文件是否存在
     */
    boolean exists(String filePath);

    /**
     * 生成预签名URL（用于直接上传到云存储）
     */
    String generatePresignedUploadUrl(String filePath, Duration expiration);

    /**
     * 获取文件的临时下载链接
     */
    String getTemporaryDownloadUrl(String filePath, Duration expiration);

}
