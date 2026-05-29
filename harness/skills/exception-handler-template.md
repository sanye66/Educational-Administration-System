# 异常处理模板

本文档提供异常处理的标准模板，基于异常处理规范生成。

## 1. 统一响应类

```java
package com.edu.common.core.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果
 *
 * @author author
 */
@Data
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 状态码 */
    private int code;

    /** 消息 */
    private String message;

    /** 数据 */
    private T data;

    /** 时间戳 */
    private long timestamp;

    public R() {
        this.timestamp = System.currentTimeMillis();
    }

    public R(int code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public R(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    // ==================== 成功响应 ====================

    public static <T> R<T> ok() {
        return new R<>(SuccessCode.SUCCESS.getCode(), SuccessCode.SUCCESS.getMessage());
    }

    public static <T> R<T> ok(T data) {
        return new R<>(SuccessCode.SUCCESS.getCode(), SuccessCode.SUCCESS.getMessage(), data);
    }

    // ==================== 失败响应 ====================

    public static <T> R<T> failed(ErrorCode errorCode) {
        return new R<>(errorCode.getCode(), errorCode.getMessage());
    }

    public static <T> R<T> failed(ServiceException e) {
        return new R<>(e.getCode(), e.getMessage());
    }

    public static <T> R<T> failed(String message) {
        return new R<>(ErrorCode.SYSTEM_ERROR.getCode(), message);
    }
}
```

## 2. 错误码枚举

```java
package com.edu.common.core.enums;

/**
 * 错误码枚举
 *
 * @author author
 */
public enum ErrorCode {

    // 系统错误 1000-1999
    SYSTEM_ERROR(1000, "系统异常，请稍后重试"),
    DATA_NOT_FOUND(1001, "数据不存在"),

    // 认证错误 2000-2999
    UNAUTHORIZED(2000, "未授权"),
    TOKEN_EXPIRED(2001, "登录已过期"),
    TOKEN_INVALID(2002, "无效的令牌"),
    USER_DISABLED(2003, "账号已被禁用"),

    // 参数错误 3000-3999
    PARAM_ERROR(3000, "参数错误"),
    PARAM_MISSING(3001, "缺少必填参数"),
    PARAM_INVALID(3002, "参数格式错误"),

    // 业务错误 4000-4999
    USER_NOT_FOUND(4001, "用户不存在"),
    USER_EXISTS(4002, "用户已存在"),
    COURSE_FULL(4003, "课程已满"),
    COURSE_NOT_FOUND(4004, "课程不存在"),

    // 第三方服务错误 5000-5999
    THIRD_PARTY_ERROR(5000, "第三方服务异常"),
    FEIGN_ERROR(5001, "服务调用失败"),
    REDIS_ERROR(5002, "缓存服务异常"),

    ;

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
```

## 3. 业务异常类

```java
package com.edu.common.core.exception;

import com.edu.common.core.enums.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务异常
 *
 * @author author
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int code;
    private String message;

    public ServiceException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ServiceException(String message) {
        super(message);
        this.code = ErrorCode.SYSTEM_ERROR.getCode();
        this.message = message;
    }
}
```

## 4. 全局异常处理器

```java
package com.edu.common.core.handler;

import com.edu.common.core.domain.R;
import com.edu.common.core.enums.ErrorCode;
import com.edu.common.core.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author author
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public R<Void> handleServiceException(ServiceException e) {
        log.warn("业务异常: code={}, message={}", e.getCode(), e.getMessage());
        return R.failed(e);
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleValidationException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数校验失败";
        log.warn("参数校验异常: {}", message);
        return R.failed(ErrorCode.PARAM_ERROR.getCode(), message);
    }

    /**
     * 处理绑定异常
     */
    @ExceptionHandler(BindException.class)
    public R<Void> handleBindException(BindException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数绑定失败";
        log.warn("参数绑定异常: {}", message);
        return R.failed(ErrorCode.PARAM_ERROR.getCode(), message);
    }

    /**
     * 处理 HTTP 消息不可读异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("HTTP消息不可读: {}", e.getMessage());
        return R.failed(ErrorCode.PARAM_ERROR.getCode(), "请求参数格式错误");
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public R<Void> handleRuntimeException(RuntimeException e) {
        log.error("运行时异常", e);
        return R.failed(ErrorCode.SYSTEM_ERROR);
    }

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return R.failed(ErrorCode.SYSTEM_ERROR);
    }
}
```

## 5. 日志记录规范

### 5.1 Service 层日志

```java
@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Override
    public User getUserById(Long id) {
        // INFO: 记录入口参数
        log.info("开始获取用户信息, userId: {}", id);

        long startTime = System.currentTimeMillis();
        try {
            User user = baseMapper.selectById(id);

            // INFO: 记录执行结果
            log.info("获取用户信息成功, userId: {}, 耗时: {}ms",
                     id, System.currentTimeMillis() - startTime);

            if (user == null) {
                // WARN: 业务异常
                log.warn("用户不存在, userId: {}", id);
                throw new ServiceException(ErrorCode.USER_NOT_FOUND);
            }

            return user;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            // ERROR: 系统异常
            log.error("获取用户信息失败, userId: {}", id, e);
            throw new ServiceException(ErrorCode.SYSTEM_ERROR);
        }
    }
}
```

### 5.2 日志级别规范

| 级别 | 使用场景 |
|------|----------|
| DEBUG | 调试信息，记录方法参数、返回值 |
| INFO | 正常业务日志，如接口调用开始/结束 |
| WARN | 业务异常，可恢复的错误 |
| ERROR | 系统异常，不可恢复的错误 |

## 6. 统一响应示例

### 成功响应
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "id": 1,
    "username": "admin"
  },
  "timestamp": 1704067200000
}
```

### 失败响应
```json
{
  "code": 4001,
  "message": "用户不存在",
  "data": null,
  "timestamp": 1704067200000
}
```
