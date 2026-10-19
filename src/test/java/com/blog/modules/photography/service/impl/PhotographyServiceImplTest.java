package com.blog.modules.photography.service.impl;

import com.blog.common.constant.FileConstants;
import com.blog.common.file.FileAccessType;
import com.blog.common.file.FileStorageServiceFactory;
import com.blog.common.file.config.FileUploadProperties;
import com.blog.common.file.service.FileStorageService;
import com.blog.modules.file.domain.entity.FileMetadata;
import com.blog.modules.file.mapper.FileMetadataMapper;
import com.blog.modules.photography.domain.entity.Photo;
import com.blog.modules.photography.domain.entity.PhotoAlbum;
import com.blog.modules.photography.mapper.PhotoAlbumMapper;
import com.blog.modules.photography.mapper.PhotoMapper;
import com.blog.modules.photography.service.PhotoImageService;
import com.blog.modules.photography.service.PhotographyConvert;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.support.TransactionTemplate;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 摄影文件流式输出测试
 *
 * @author xuesong.lei
 * @since 2026-10-19
 */
class PhotographyServiceImplTest {

    @Test
    void originalIsWrittenWhileReadingAndStorageStreamIsClosed() {
        PhotoAlbumMapper albums = mock(PhotoAlbumMapper.class);
        PhotoMapper photos = mock(PhotoMapper.class);
        FileMetadataMapper files = mock(FileMetadataMapper.class);
        FileStorageService storage = mock(FileStorageService.class);
        PhotoAlbum album = new PhotoAlbum();
        album.setId(1L);
        album.setStatus("PUBLISHED");
        album.setPublishedJson("{\"title\":\"相册\",\"photos\":[{\"photoId\":2}]}");
        when(albums.selectById(1L)).thenReturn(album);
        Photo photo = new Photo().setId(2L).setAlbumId(1L).setOriginalFileId(3L);
        when(photos.selectById(2L)).thenReturn(photo);
        when(files.selectById(3L)).thenReturn(new FileMetadata().setId(3L).setFilePath("photos/original.jpg")
                .setPlatform("LOCAL").setFileSize(6L).setAccessType(FileAccessType.PRIVATE));
        ByteArrayOutputStream written = new ByteArrayOutputStream();
        AtomicBoolean closed = new AtomicBoolean();
        InputStream source = new InputStream() {
            private int reads;

            @Override
            public int read() {
                throw new UnsupportedOperationException();
            }

            @Override
            public int read(byte[] buffer, int offset, int length) {
                if (reads == 1) {
                    assertEquals(3, written.size(), "下一块数据读取前应已输出上一块，不能先缓冲整个原图");
                }
                if (reads++ >= 2) {
                    return -1;
                }
                for (int i = 0; i < 3; i++) {
                    buffer[offset + i] = (byte) ((reads - 1) * 3 + i);
                }
                return 3;
            }

            @Override
            public void close() {
                closed.set(true);
            }
        };
        when(storage.download("photos/original.jpg")).thenReturn(source);
        FileStorageServiceFactory factory = new FileStorageServiceFactory(new FileUploadProperties(),
                Collections.singletonMap(FileConstants.LOCAL, storage));
        PhotographyServiceImpl service = new PhotographyServiceImpl(albums, photos,
                mock(PhotographyConvert.class), new ObjectMapper(), mock(TransactionTemplate.class), factory,
                files, mock(PhotoImageService.class));
        MockHttpServletResponse response = new MockHttpServletResponse() {
            @Override
            public ServletOutputStream getOutputStream() {
                return new ServletOutputStream() {
                    @Override
                    public boolean isReady() { return true; }
                    @Override
                    public void setWriteListener(WriteListener listener) { }
                    @Override
                    public void write(int value) { written.write(value); }
                };
            }
        };
        service.preview(1L, 2L, "original", true, response);
        assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5}, written.toByteArray());
        assertTrue(closed.get());
        assertEquals(6L, response.getContentLengthLong());
        assertEquals("private, no-store", response.getHeader("Cache-Control"));
        assertTrue(response.getHeader("Content-Disposition").contains("attachment"));
    }
}
