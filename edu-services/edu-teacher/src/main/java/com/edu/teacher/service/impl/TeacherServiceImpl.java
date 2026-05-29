package com.edu.teacher.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.common.core.exception.BusinessException;
import com.edu.common.core.result.R;
import com.edu.common.core.result.ResultCode;
import com.edu.common.log.annotation.Log;
import com.edu.common.log.annotation.OperateType;
import com.edu.teacher.entity.Teacher;
import com.edu.teacher.mapper.TeacherMapper;
import com.edu.teacher.service.TeacherService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Override
    public R<?> getTeacherById(Long id) {
        Teacher teacher = this.getById(id);
        if (teacher == null) {
            throw new BusinessException(ResultCode.TEACHER_NOT_FOUND);
        }
        return R.ok(teacher);
    }

    @Override
    public R<?> getTeacherByUserId(Long userId) {
        Teacher teacher = this.getOne(
                new LambdaQueryWrapper<Teacher>().eq(Teacher::getUserId, userId));
        return R.ok(teacher);
    }

    @Override
    public R<?> listTeachers() {
        List<Teacher> list = this.list();
        return R.ok(list);
    }

    @Override
    @Log(value = "创建教师", operateType = OperateType.INSERT)
    public R<?> createTeacher(Teacher teacher) {
        // 验证必填字段
        if (teacher.getUserId() == null) {
            return R.failed("用户ID不能为空");
        }
        if (teacher.getTeacherNo() == null || teacher.getTeacherNo().trim().isEmpty()) {
            return R.failed("教师工号不能为空");
        }
        
        // 检查工号是否已存在
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teacher::getTeacherNo, teacher.getTeacherNo());
        long count = this.count(queryWrapper);
        if (count > 0) {
            return R.failed("教师工号已存在");
        }
        
        this.save(teacher);
        return R.ok();
    }

    @Override
    @Log(value = "更新教师", operateType = OperateType.UPDATE)
    public R<?> updateTeacher(Teacher teacher) {
        // 验证教师ID
        if (teacher.getId() == null) {
            return R.failed("教师ID不能为空");
        }
        
        // 检查教师是否存在
        Teacher existingTeacher = this.getById(teacher.getId());
        if (existingTeacher == null) {
            return R.failed("教师不存在");
        }
        
        // 执行更新
        boolean success = this.updateById(teacher);
        if (!success) {
            return R.failed("更新教师失败");
        }
        return R.ok();
    }

    @Override
    @Log(value = "删除教师", operateType = OperateType.DELETE)
    public R<?> deleteTeacher(Long id) {
        // 验证教师ID
        if (id == null) {
            return R.failed("教师ID不能为空");
        }
        
        // 检查教师是否存在
        Teacher existingTeacher = this.getById(id);
        if (existingTeacher == null) {
            return R.failed("教师不存在");
        }
        
        // 执行删除
        boolean success = this.removeById(id);
        if (!success) {
            return R.failed("删除教师失败");
        }
        return R.ok();
    }

    @Override
    public R<?> searchTeachers(String keyword) {
        LambdaQueryWrapper<Teacher> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.like(Teacher::getTeacherNo, keyword)
                    .or()
                    .like(Teacher::getResearchDirection, keyword);
        }
        return R.ok(this.list(wrapper));
    }
}
