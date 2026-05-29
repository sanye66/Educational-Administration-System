package com.edu.common.core.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应状态码枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),

    // 客户端错误 4xx
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未认证"),
    FORBIDDEN(403, "无权限"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    CONFLICT(409, "资源冲突"),
    GONE(410, "资源已被删除"),
    UNPROCESSABLE_ENTITY(422, "参数校验失败"),
    TOO_MANY_REQUESTS(429, "请求过于频繁"),

    // 业务错误 1xxx
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_PASSWORD_ERROR(1002, "密码错误"),
    USER_ACCOUNT_DISABLED(1003, "账号已被禁用"),
    USER_ACCOUNT_LOCKED(1004, "账号已被锁定"),
    TOKEN_INVALID(1005, "Token无效"),
    TOKEN_EXPIRED(1006, "Token已过期"),
    PARAM_ERROR(1007, "参数错误"),

    // 教师相关 11xx
    TEACHER_NOT_FOUND(1101, "教师不存在"),

    // 学生相关 12xx
    STUDENT_NOT_FOUND(1201, "学生不存在"),

    // 课程相关 2xxx
    COURSE_NOT_FOUND(2001, "课程不存在"),
    COURSE_CAPACITY_FULL(2002, "课程已满"),

    // 选课相关 3xxx
    SELECTION_TIME_INVALID(3001, "不在选课时间内"),
    SELECTION_ALREADY_SELECTED(3002, "已选择该课程"),
    SELECTION_CREDIT_EXCEEDED(3003, "超出学分上限"),

    // 成绩相关 4xxx
    GRADE_NOT_FOUND(4001, "成绩不存在"),
    GRADE_ALREADY_EXISTS(4002, "成绩已录入"),

    // 排课相关 5xxx
    SCHEDULE_CONFLICT(5001, "排课冲突"),

    // 考试相关 6xxx
    EXAM_NOT_FOUND(6001, "考试不存在"),
    EXAM_CONFLICT(6002, "考试时间冲突"),

    // 毕设相关 7xxx
    GRADUATION_TOPIC_NOT_FOUND(7001, "毕设题目不存在"),
    GRADUATION_ALREADY_SELECTED(7002, "已选择毕设题目"),

    // 评价相关 8xxx
    EVALUATION_NOT_FOUND(8001, "评价不存在"),
    EVALUATION_ALREADY_SUBMITTED(8002, "已提交评价");

    private final int code;
    private final String msg;
}
