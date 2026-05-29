package com.edu.auth.controller;

import com.edu.common.core.constant.SystemConstants;
import com.edu.common.core.result.R;
import com.edu.common.core.result.ResultCode;
import com.edu.common.log.annotation.Log;
import com.edu.common.log.annotation.OperateType;
import com.edu.common.redis.util.RedisUtil;
import com.edu.common.security.domain.LoginUser;
import com.edu.common.security.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * 认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "登录、注册、Token刷新")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    /**
     * 登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    @Log(value = "用户登录", operateType = OperateType.SELECT)
    public R<LoginUser> login(@RequestBody LoginRequest loginRequest) {
        // TODO: 实际从用户服务校验，此处为框架骨架
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(1L);
        loginUser.setUsername(loginRequest.getUsername());
        loginUser.setRealName("管理员");
        loginUser.setUserType("ADMIN");
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(System.currentTimeMillis() + 86400000L);

        // 生成Token
        String token = jwtUtil.generateToken(loginUser);
        loginUser.setToken(token);

        // 缓存登录信息到Redis
        String tokenKey = SystemConstants.LOGIN_TOKEN_KEY + token;
        redisUtil.set(tokenKey, loginUser, 24, TimeUnit.HOURS);

        return R.ok(loginUser);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    @Log(value = "用户注册", operateType = OperateType.INSERT)
    public R<Void> register(@RequestBody RegisterRequest registerRequest) {
        // TODO: 调用用户服务创建用户
        return R.ok();
    }

    /**
     * 刷新Token
     */
    @PostMapping("/refresh")
    @Operation(summary = "刷新Token")
    public R<LoginUser> refresh(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace(SystemConstants.TOKEN_PREFIX, "");
        if (jwtUtil.isTokenExpired(token)) {
            return R.failed(ResultCode.TOKEN_EXPIRED);
        }

        String username = jwtUtil.getUsernameFromToken(token);
        String tokenKey = SystemConstants.LOGIN_TOKEN_KEY + token;
        LoginUser loginUser = redisUtil.get(tokenKey, LoginUser.class);

        if (loginUser == null) {
            return R.failed(ResultCode.TOKEN_INVALID);
        }

        // 生成新Token
        String newToken = jwtUtil.generateToken(loginUser);
        loginUser.setToken(newToken);

        // 删除旧Token，缓存新Token
        redisUtil.delete(tokenKey);
        redisUtil.set(SystemConstants.LOGIN_TOKEN_KEY + newToken, loginUser, 24, TimeUnit.HOURS);

        return R.ok(loginUser);
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出")
    @Log(value = "用户登出", operateType = OperateType.SELECT)
    public R<Void> logout(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace(SystemConstants.TOKEN_PREFIX, "");
        redisUtil.delete(SystemConstants.LOGIN_TOKEN_KEY + token);
        return R.ok();
    }

    /**
     * 登录请求
     */
    @lombok.Data
    public static class LoginRequest {
        private String username;
        private String password;
        private String captchaKey;
        private String captchaCode;
    }

    /**
     * 注册请求
     */
    @lombok.Data
    public static class RegisterRequest {
        private String username;
        private String password;
        private String realName;
        private String userType;
        private String email;
        private String phone;
    }
}
