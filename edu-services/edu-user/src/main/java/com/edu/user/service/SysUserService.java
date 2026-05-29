package com.edu.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.common.core.domain.PageRequest;
import com.edu.common.core.result.R;
import com.edu.user.entity.SysUser;

public interface SysUserService extends IService<SysUser> {

    R<?> getUserById(Long id);

    R<?> getUserByUsername(String username);

    R<?> listUsers(PageRequest pageRequest);

    R<?> createUser(SysUser user);

    R<?> updateUser(SysUser user);

    R<?> deleteUser(Long id);
}
