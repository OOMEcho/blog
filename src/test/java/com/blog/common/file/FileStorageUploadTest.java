package com.blog.common.file;

import com.blog.common.exception.BusinessException;
import com.blog.common.file.config.FileUploadProperties;
import com.blog.common.file.service.impl.LocalFileStorageServiceImpl;
import com.blog.modules.file.domain.entity.FileMetadata;
import com.blog.modules.file.mapper.FileMetadataMapper;
import com.blog.modules.user.domain.entity.User;
import com.blog.utils.SecurityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 文件上传访问属性与原有调用兼容测试
 *
 * @author xuesong.lei
 * @since 2026-10-19
 */
class FileStorageUploadTest {

    @TempDir
    Path root;

    private FileMetadataMapper files;
    private LocalFileStorageServiceImpl storage;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        files = mock(FileMetadataMapper.class);
        FileUploadProperties properties = new FileUploadProperties();
        properties.getLocal().setPath(root.toString());
        storage = new LocalFileStorageServiceImpl(properties, files);
        ThreadLocal<User> currentUser = (ThreadLocal<User>) ReflectionTestUtils
                .getField(SecurityUtils.class, "CURRENT_USER_CACHE");
        currentUser.set(new User().setId(1L));
    }

    @AfterEach
    void tearDown() {
        SecurityUtils.clearCurrentUser();
    }

    @Test
    void existingUploadIsPublicEvenInPhotographyDirectory() {
        FileMetadata metadata = storage.upload(file(), "photography");
        assertEquals(FileAccessType.PUBLIC, metadata.getAccessType());
        verify(files).insert(metadata);
    }

    @ParameterizedTest
    @EnumSource(FileAccessType.class)
    void requestedAccessTypeIsPresentAtFirstInsertAndBytesAreUnchanged(FileAccessType accessType) throws Exception {
        doAnswer(invocation -> {
            FileMetadata metadata = invocation.getArgument(0);
            assertEquals(accessType, metadata.getAccessType());
            return 1;
        }).when(files).insert(any(FileMetadata.class));
        ByteArrayMultipartFile file = file();
        FileMetadata metadata = storage.upload(file, "articles", accessType);
        assertEquals(accessType, metadata.getAccessType());
        assertArrayEquals(file.getBytes(), Files.readAllBytes(java.nio.file.Paths.get(metadata.getFilePath())));
        verify(files, times(1)).insert(metadata);
        verify(files, never()).updateById(any(FileMetadata.class));
    }

    @Test
    void missingAccessTypeIsRejectedBeforeSavingBytesOrMetadata() throws Exception {
        assertThrows(BusinessException.class, () -> storage.upload(file(), "articles", null));
        verifyNoInteractions(files);
        try (java.util.stream.Stream<Path> paths = Files.walk(root)) {
            assertFalse(paths.anyMatch(Files::isRegularFile));
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    void fileMapperPersistsAndReadsAccessTypeAsEnumName() throws Exception {
        com.baomidou.mybatisplus.core.MybatisConfiguration configuration = new com.baomidou.mybatisplus.core.MybatisConfiguration();
        configuration.addMapper(FileMetadataMapper.class);
        com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean factory =
                new com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean();
        factory.setConfiguration(configuration);
        factory.setDataSource(mock(javax.sql.DataSource.class));
        org.apache.ibatis.session.Configuration mapped = factory.getObject().getConfiguration();
        org.apache.ibatis.mapping.ResultMapping access = mapped
                .getResultMap(FileMetadataMapper.class.getName() + ".BaseResultMap")
                .getResultMappings().stream().filter(field -> "accessType".equals(field.getProperty()))
                .findFirst().orElseThrow(AssertionError::new);
        org.apache.ibatis.type.TypeHandler<FileAccessType> handler =
                (org.apache.ibatis.type.TypeHandler<FileAccessType>) access.getTypeHandler();
        java.sql.PreparedStatement statement = mock(java.sql.PreparedStatement.class);
        handler.setParameter(statement, 1, FileAccessType.PRIVATE, null);
        verify(statement).setString(1, "PRIVATE");
        java.sql.ResultSet result = mock(java.sql.ResultSet.class);
        when(result.getString("access_type")).thenReturn("PRIVATE");
        assertEquals(FileAccessType.PRIVATE, handler.getResult(result, "access_type"));
        String select = mapped.getMappedStatement(FileMetadataMapper.class.getName() + ".selectById")
                .getBoundSql(1L).getSql();
        assertTrue(select.contains("access_type"));
    }

    private ByteArrayMultipartFile file() {
        return new ByteArrayMultipartFile("camera.jpg", "image/jpeg",
                new byte[]{(byte) 0xff, (byte) 0xd8, (byte) 0xff, 0x00});
    }
}
