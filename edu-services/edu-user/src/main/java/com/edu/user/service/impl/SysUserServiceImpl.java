package com.edu.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.common.core.domain.PageRequest;
import com.edu.common.core.exception.BusinessException;
import com.edu.common.core.result.R;
import com.edu.common.core.result.ResultCode;
import com.edu.common.log.annotation.Log;
import com.edu.common.log.annotation.OperateType;
import com.edu.user.entity.SysUser;
import com.edu.user.mapper.SysUserMapper;
import com.edu.user.service.SysUserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public R<?> getUserById(Long id) {
        SysUser user = this.getById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        user.setPassword(null);
        return R.ok(user);
    }

    @Override
    public R<?> getUserByUsername(String username) {
        SysUser user = this.getOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        user.setPassword(null);
        return R.ok(user);
    }

    @Override
    public R<?> listUsers(PageRequest pageRequest) {
        Page<SysUser> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        Page<SysUser> result = this.page(page);
        result.getRecords().forEach(u -> u.setPassword(null));
        return R.ok(result);
    }

    @Override
    @Log(value = "创建用户", operateType = OperateType.INSERT)
    public R<?> createUser(SysUser user) {
        // 验证必填字段
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return R.failed("用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return R.failed("密码不能为空");
        }
        if (user.getRealName() == null || user.getRealName().trim().isEmpty()) {
            return R.failed("真实姓名不能为空");
        }
        if (user.getUserType() == null || user.getUserType().trim().isEmpty()) {
            return R.failed("用户类型不能为空");
        }
        
        // 检查用户名是否已存在
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, user.getUsername());
        long count = this.count(queryWrapper);
        if (count > 0) {
            return R.failed("用户名已存在");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.save(user);
        return R.ok();
    }

    @Override
    @Log(value = "更新用户", operateType = OperateType.UPDATE)
    public R<?> updateUser(SysUser user) {
        // 验证用户ID
        if (user.getId() == null) {
            return R.failed("用户ID不能为空");
        }
        
        // 检查用户是否存在
        SysUser existingUser = this.getById(user.getId());
        if (existingUser == null) {
            return R.failed("用户不存在");
        }
        
        // 如果提供了新密码，则加密；否则保持原密码不变
        if (StrUtil.isNotBlank(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // 不更新密码字段，保持数据库中的原密码
            user.setPassword(existingUser.getPassword());
        }
        
        // 执行更新
        boolean success = this.updateById(user);
        if (!success) {
            return R.failed("更新用户失败");
        }
        return R.ok();
    }

    @Override
    @Log(value = "删除用户", operateType = OperateType.DELETE)
    public R<?> deleteUser(Long id) {
        // 验证用户ID
        if (id == null) {
            return R.failed("用户ID不能为空");
        }
        
        // 检查用户是否存在
        SysUser existingUser = this.getById(id);
        if (existingUser == null) {
            return R.failed("用户不存在");
        }
        
        // 防止删除管理员账户（可选的安全措施）
        if ("admin".equals(existingUser.getUsername())) {
            return R.failed("不能删除管理员账户");
        }
        
        // 执行删除
        boolean success = this.removeById(id);
        if (!success) {
            return R.failed("删除用户失败");
        }
        return R.ok();
    }
}
