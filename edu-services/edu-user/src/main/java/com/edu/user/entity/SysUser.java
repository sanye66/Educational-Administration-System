package com.edu.user.entity;

import com.edu.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统用户实体
 */
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_user")
public class SysUser extends BaseEntity {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 用户类型: ADMIN/TEACHER/STUDENT
     */
    private String userType;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别: 0-男 1-女 2-未知
     */
    private Integer gender;

    /**
     * 状态: 0-正常 1-禁用 2-锁定
     */
    private Integer status;
}
