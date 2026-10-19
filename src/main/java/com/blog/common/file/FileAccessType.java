package com.blog.common.file;

/**
 * 文件访问类型枚举
 *
 * @author xuesong.lei
 * @since 2026-10-19
 */
public enum FileAccessType {

    /**
     * 公开文件，允许通过通用文件接口访问
     */
    PUBLIC,

    /**
     * 私有文件，由所属业务校验后读取和维护
     */
    PRIVATE
}
