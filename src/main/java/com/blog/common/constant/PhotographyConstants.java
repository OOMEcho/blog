package com.blog.common.constant;

/**
 * 摄影功能常量
 *
 * @author xuesong.lei
 * @since 2026-10-19
 */
public final class PhotographyConstants {

    /**
     * 摄影存储目录，仅用于文件组织及部署配置
     */
    public static final String DIRECTORY = "photography";

    /**
     * 单张照片最大文件大小，单位字节
     */
    public static final long MAX_FILE_SIZE = 50L * 1024 * 1024;

    /**
     * 照片最大像素数量
     */
    public static final long MAX_PIXELS = 100_000_000L;

    private PhotographyConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
