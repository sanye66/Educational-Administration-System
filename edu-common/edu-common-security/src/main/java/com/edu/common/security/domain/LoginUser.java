package com.edu.common.security.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 登录用户信息
 */
@Data
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 用户类型: ADMIN/TEACHER/STUDENT
     */
    private String userType;

    /**
     * 角色ID集合
     */
    private Set<Long> roleIds;

    /**
     * 权限标识集合
     */
    private Set<String> permissions;

    /**
     * Token
     */
    private String token;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;
}
