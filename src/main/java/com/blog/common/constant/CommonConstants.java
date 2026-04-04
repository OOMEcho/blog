package com.blog.common.constant;

/**
 * 通用常量
 *
 * @author xuesong.lei
 * @since 2025-08-21
 */
public class CommonConstants {

    /**
     * 访问令牌请求头
     */
    public static final String REFRESH_TOKEN_COOKIE = "refresh_token";

    /**
     * token前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 用户代理请求头
     */
    public static final String USER_AGENT = "User-Agent";

    /**
     * 所有请求方式
     */
    public static final String REQUEST_METHOD_ALL = "all";

    /**
     * 正常/成功
     */
    public static final String NORMAL_STATUS = "0";

    /**
     * 停用/失败
     */
    public static final String DISABLE_STATUS = "1";

    /**
     * 超级管理员角色编码
     */
    public static final String ADMIN_ROLE = "admin";

    /**
     * 无权限标识
     */
    public static final String NONE = "none";

    /**
     * 匿名用户标识
     */
    public static final String ANONYMOUS = "anonymous";

    /**
     * 默认密码
     */
    public static final String DEFAULT_PASSWORD = "123456";

    /**
     * 超级管理员ID
     */
    public static final Long SUPER_ADMIN_ID = 1L;

    /**
     * 操作成功消息
     */
    public static final String SUCCESS_MESSAGE = "操作成功";
}
