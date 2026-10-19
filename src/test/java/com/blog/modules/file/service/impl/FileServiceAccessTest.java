package com.blog.modules.file.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.blog.common.constant.FileConstants;
import com.blog.common.exception.BusinessException;
import com.blog.common.file.FileStorageServiceFactory;
import com.blog.common.file.FileAccessType;
import com.blog.common.file.config.FileUploadProperties;
import com.blog.common.file.service.FileStorageService;
import com.blog.modules.file.domain.dto.PresignedUploadCompleteDTO;
import com.blog.modules.file.domain.entity.FileMetadata;
import com.blog.modules.file.mapper.FileMetadataMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 文件访问属性隔离与公开文件兼容测试
 *
 * @author xuesong.lei
 * @since 2026-10-19
 */
class FileServiceAccessTest {

    private FileMetadataMapper files;
    private FileStorageService storage;
    private FileServiceImpl service;
    private FileMetadata metadata;

    @BeforeEach
    void setUp() {
        files = mock(FileMetadataMapper.class);
        storage = mock(FileStorageService.class);
        FileUploadProperties properties = new FileUploadProperties();
        properties.setCdnBaseUrl("https://cdn.example.test");
        properties.setPublicBaseUrl("https://blog.example.test");
        service = new FileServiceImpl(new FileStorageServiceFactory(properties,
                Collections.singletonMap(FileConstants.LOCAL, storage)), files, properties);
        ReflectionTestUtils.setField(service, "basePath", "/uploads");
        ReflectionTestUtils.setField(service, "secretKey", "test-signing-key");
        metadata = new FileMetadata().setId(1L).setPlatform("LOCAL").setSuffix("jpg")
                .setOriginalFileName("camera.jpg").setAccessType(FileAccessType.PRIVATE)
                .setFilePath("/uploads/articles/2026/10/19/test.jpg");
        when(files.selectById(1L)).thenReturn(metadata);
        when(files.selectOne(any())).thenReturn(metadata);
    }

    @Test
    void privateFilesCannotUseGenericPreviewDownloadSignedLinkOrDelete() {
        assertThrows(BusinessException.class, () -> service.preview(1L, new MockHttpServletResponse()));
        assertThrows(BusinessException.class, () -> service.download(metadata.getFilePath(), new MockHttpServletResponse()));
        assertThrows(BusinessException.class, () -> service.getTemporaryDownloadUrl(metadata.getFilePath(), 60));
        assertThrows(BusinessException.class, () -> service.deleteFile(metadata.getFilePath()));
        verifyNoInteractions(storage);
        verify(files, never()).deleteById(anyLong());
    }

    @Test
    void privateFilesCannotRedirectToCdnOrResolvePublicUrl() {
        metadata.setPlatform("MINIO").setFilePath("photography/2026/10/19/test.jpg");
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertThrows(BusinessException.class, () -> service.preview(1L, response));
        assertNull(response.getRedirectedUrl());
        assertNull(service.resolvePublicAccessUrl(metadata.getFilePath()));
        verifyNoInteractions(storage);
    }

    @Test
    void validLocalSignatureDoesNotBypassPrivateAccess() {
        String expires = String.valueOf(System.currentTimeMillis() + 60000);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/file/localDownload/articles/2026/10/19/test.jpg");
        request.setParameter("expires", expires);
        request.setParameter("token", SecureUtil.hmacSha256("test-signing-key")
                .digestHex(metadata.getFilePath() + expires));
        assertEquals(404, assertThrows(BusinessException.class,
                () -> service.localDownload(request, new MockHttpServletResponse())).getCode().intValue());
        verifyNoInteractions(storage);
    }

    @Test
    void presignedCompletionCannotMakeExistingPrivateFilePublic() {
        PresignedUploadCompleteDTO dto = new PresignedUploadCompleteDTO();
        dto.setFilePath(metadata.getFilePath());
        assertEquals(404, assertThrows(BusinessException.class,
                () -> service.completePresignedUpload(dto)).getCode().intValue());
        assertEquals(FileAccessType.PRIVATE, metadata.getAccessType());
        verify(files, never()).insert(any(FileMetadata.class));
        verifyNoInteractions(storage);
    }

    @Test
    void ordinaryUploadsMayUsePhotographyDirectoryWithoutChangingAccessRules() {
        metadata.setAccessType(FileAccessType.PUBLIC);
        when(storage.upload(any(), eq("photography"))).thenReturn(metadata);
        assertSame(metadata, service.uploadFile(null, "photography"));
        assertEquals("https://blog.example.test/file/preview/1", metadata.getAccessUrl());
        verify(storage).upload(null, "photography");
    }

    @Test
    void missingAccessTypeIsNotTreatedAsPublic() {
        metadata.setAccessType(null);
        assertThrows(BusinessException.class, () -> service.preview(1L, new MockHttpServletResponse()));
        assertNull(service.resolvePublicAccessUrl(metadata.getFilePath()));
        verifyNoInteractions(storage);
    }

    @Test
    void ordinaryImagesKeepCdnPreviewAndLocalStreaming() throws Exception {
        metadata.setAccessType(FileAccessType.PUBLIC);
        metadata.setPlatform("MINIO").setFilePath("photography/2026/10/19/test.jpg");
        MockHttpServletResponse cdn = new MockHttpServletResponse();
        service.preview(1L, cdn);
        assertEquals("https://cdn.example.test/photography/2026/10/19/test.jpg", cdn.getRedirectedUrl());
        metadata.setPlatform("LOCAL").setFilePath("/uploads/articles/2026/10/19/test.jpg");
        byte[] image = new byte[]{1, 2, 3};
        when(storage.download(metadata.getFilePath())).thenReturn(new ByteArrayInputStream(image));
        MockHttpServletResponse local = new MockHttpServletResponse();
        service.preview(1L, local);
        assertArrayEquals(image, local.getContentAsByteArray());
        assertEquals("public, max-age=3600", local.getHeader("Cache-Control"));
    }

    @Test
    void publicFilesKeepDownloadTemporaryUrlAndDeleteBehavior() throws Exception {
        metadata.setAccessType(FileAccessType.PUBLIC);
        byte[] bytes = new byte[]{1, 2, 3};
        when(storage.download(metadata.getFilePath())).thenAnswer(invocation -> new ByteArrayInputStream(bytes));
        MockHttpServletResponse downloaded = new MockHttpServletResponse();
        service.download(metadata.getFilePath(), downloaded);
        assertArrayEquals(bytes, downloaded.getContentAsByteArray());

        String expires = String.valueOf(System.currentTimeMillis() + 60000);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/file/localDownload/articles/2026/10/19/test.jpg");
        request.setParameter("expires", expires);
        request.setParameter("token", SecureUtil.hmacSha256("test-signing-key")
                .digestHex(metadata.getFilePath() + expires));
        MockHttpServletResponse local = new MockHttpServletResponse();
        service.localDownload(request, local);
        assertArrayEquals(bytes, local.getContentAsByteArray());

        when(storage.exists(metadata.getFilePath())).thenReturn(true);
        when(storage.getTemporaryDownloadUrl(eq(metadata.getFilePath()), any()))
                .thenReturn("https://storage.example.test/signed");
        assertEquals("https://storage.example.test/signed",
                service.getTemporaryDownloadUrl(metadata.getFilePath(), 60).get("downloadUrl"));
        assertEquals(metadata.getFilePath(), service.deleteFile(metadata.getFilePath()));
        verify(storage).delete(metadata.getFilePath());
        verify(files).deleteById(metadata.getId());
    }

}
