package com.edu.student.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.common.core.exception.BusinessException;
import com.edu.common.core.result.R;
import com.edu.common.core.result.ResultCode;
import com.edu.common.log.annotation.Log;
import com.edu.common.log.annotation.OperateType;
import com.edu.student.entity.Student;
import com.edu.student.mapper.StudentMapper;
import com.edu.student.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Override
    public R<?> getStudentById(Long id) {
        Student student = this.getById(id);
        if (student == null) {
            throw new BusinessException(ResultCode.STUDENT_NOT_FOUND);
        }
        return R.ok(student);
    }

    @Override
    public R<?> getStudentByUserId(Long userId) {
        Student student = this.getOne(
                new LambdaQueryWrapper<Student>().eq(Student::getUserId, userId));
        return R.ok(student);
    }

    @Override
    public R<?> listStudents() {
        List<Student> list = this.list();
        return R.ok(list);
    }

    @Override
    @Log(value = "创建学生", operateType = OperateType.INSERT)
    public R<?> createStudent(Student student) {
        this.save(student);
        return R.ok();
    }

    @Override
    @Log(value = "更新学生", operateType = OperateType.UPDATE)
    public R<?> updateStudent(Student student) {
        if (student.getId() == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR);
        }
        this.updateById(student);
        return R.ok();
    }

    @Override
    @Log(value = "删除学生", operateType = OperateType.DELETE)
    public R<?> deleteStudent(Long id) {
        this.removeById(id);
        return R.ok();
    }

    @Override
    public R<?> searchStudents(String keyword) {
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.like(Student::getStudentNo, keyword)
                    .or()
                    .like(Student::getStatus, keyword);
        }
        return R.ok(this.list(wrapper));
    }
}
