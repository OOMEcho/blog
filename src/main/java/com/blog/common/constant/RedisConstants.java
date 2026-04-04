package com.blog.common.constant;

/**
 * Redis常量
 *
 * @author xuesong.lei
 * @since 2025-08-31
 */
public class RedisConstants {

    /**
     * 防止重复提交 key 前缀
     */
    public static final String REPEAT_SUBMIT = "repeat_submit:";

    /**
     * 限流 key 前缀
     */
    public static final String RATE_LIMIT = "rate_limit:";

    /**
     * redis中存储的资源key（用于鉴权）
     */
    public static final String RESOURCES = "resources";

    /**
     * redis中存储的白名单key
     */
    public static final String WHITELIST = "whitelist";

    /**
     * Access token session key 前缀
     */
    public static final String SESSION = "session:";

    /**
     * 用户当前 session 反向索引（用于单设备登录控制）
     */
    public static final String USER_SESSION = "user_session:";

    /**
     * Refresh token key 前缀
     */
    public static final String REFRESH = "refresh:";

    /**
     * 用户当前 refresh token 反向索引
     */
    public static final String USER_REFRESH = "user_refresh:";

    /**
     * 滑块验证码 key
     */
    public static final String SLIDER_CAPTCHA_KEY = "captcha:";

    /**
     * 邮箱登录 key
     */
    public static final String EMAIL_LOGIN = "emailLogin:";

    /**
     * 邮箱登录错误次数 key
     */
    public static final String EMAIL_LOGIN_ERROR = "emailLoginError:";

    /**
     * 邮箱发送频率 key
     */
    public static final String EMAIL_SEND_FREQUENCY = "emailSendFrequency:";

    /**
     * 邮箱每日发送上限 key
     */
    public static final String EMAIL_DAILY_LIMIT = "emailDailyLimit:";

    /**
     * 资源分布式锁 key
     */
    public static final String RESOURCE_LOCK_KEY = "lock:security:resources";

    /**
     * 白名单分布式锁 key
     */
    public static final String WHITELIST_LOCK_KEY = "lock:security:whitelist";

}
