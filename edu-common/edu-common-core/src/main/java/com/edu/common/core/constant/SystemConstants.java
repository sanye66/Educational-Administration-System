package com.edu.common.core.constant;

/**
 * 系统常量
 */
public class SystemConstants {

    private SystemConstants() {}

    /**
     * 成功标记
     */
    public static final int SUCCESS = 200;

    /**
     * 失败标记
     */
    public static final int FAIL = 500;

    /**
     * 登录用户Redis Key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 验证码Redis Key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 登录用户ID Header
     */
    public static final String USER_ID_HEADER = "X-User-Id";

    /**
     * 登录用户名 Header
     */
    public static final String USERNAME_HEADER = "X-Username";

    /**
     * Token Header
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * Token 前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 超级管理员角色ID
     */
    public static final Long SUPER_ADMIN_ROLE_ID = 1L;

    /**
     * 默认密码
     */
    public static final String DEFAULT_PASSWORD = "123456";

    /**
     * 选课Redis锁Key前缀
     */
    public static final String SELECTION_LOCK_KEY = "selection:lock:";

    /**
     * 课程容量Redis Key前缀
     */
    public static final String COURSE_CAPACITY_KEY = "course:capacity:";
}
