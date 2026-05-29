package com.edu.user.controller;

import com.edu.common.core.domain.PageRequest;
import com.edu.common.core.result.R;
import com.edu.user.entity.SysUser;
import com.edu.user.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户CRUD接口")
public class SysUserController {

    private final SysUserService sysUserService;

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询用户")
    public R<?> getUserById(@PathVariable Long id) {
        return sysUserService.getUserById(id);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "根据用户名查询用户")
    public R<?> getUserByUsername(@PathVariable String username) {
        return sysUserService.getUserByUsername(username);
    }

    @GetMapping("/list")
    @Operation(summary = "分页查询用户列表")
    public R<?> listUsers(PageRequest pageRequest) {
        return sysUserService.listUsers(pageRequest);
    }

    @PostMapping
    @Operation(summary = "创建用户")
    public R<?> createUser(@RequestBody SysUser user) {
        return sysUserService.createUser(user);
    }

    @PutMapping
    @Operation(summary = "更新用户")
    public R<?> updateUser(@RequestBody SysUser user) {
        return sysUserService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    public R<?> deleteUser(@PathVariable Long id) {
        return sysUserService.deleteUser(id);
    }
}
