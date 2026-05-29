package com.edu.grade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.common.core.exception.BusinessException;
import com.edu.common.core.result.R;
import com.edu.common.core.result.ResultCode;
import com.edu.common.log.annotation.Log;
import com.edu.common.log.annotation.OperateType;
import com.edu.grade.entity.Grade;
import com.edu.grade.mapper.GradeMapper;
import com.edu.grade.service.GradeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {

    @Override
    public R<?> getGradeById(Long id) {
        Grade grade = this.getById(id);
        if (grade == null) {
            throw new BusinessException(ResultCode.GRADE_NOT_FOUND);
        }
        return R.ok(grade);
    }

    @Override
    public R<?> listGrades() {
        List<Grade> list = this.list();
        return R.ok(list);
    }

    @Override
    @Log(value = "录入成绩", operateType = OperateType.INSERT)
    public R<?> createGrade(Grade grade) {
        LambdaQueryWrapper<Grade> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Grade::getStudentId, grade.getStudentId())
                .eq(Grade::getCourseId, grade.getCourseId());
        if (this.count(wrapper) > 0) {
            throw new BusinessException(ResultCode.GRADE_ALREADY_EXISTS);
        }
        this.save(grade);
        return R.ok();
    }

    @Override
    @Log(value = "更新成绩", operateType = OperateType.UPDATE)
    public R<?> updateGrade(Grade grade) {
        if (grade.getId() == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR);
        }
        this.updateById(grade);
        return R.ok();
    }

    @Override
    @Log(value = "删除成绩", operateType = OperateType.DELETE)
    public R<?> deleteGrade(Long id) {
        this.removeById(id);
        return R.ok();
    }

    @Override
    public R<?> getGradesByStudentId(Long studentId) {
        LambdaQueryWrapper<Grade> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Grade::getStudentId, studentId);
        return R.ok(this.list(wrapper));
    }

    @Override
    public R<?> getGradesByCourseId(Long courseId) {
        LambdaQueryWrapper<Grade> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Grade::getCourseId, courseId);
        return R.ok(this.list(wrapper));
    }

    @Override
    @Log(value = "批量导入成绩", operateType = OperateType.INSERT)
    public R<?> batchImportGrades(List<Grade> grades) {
        this.saveBatch(grades);
        return R.ok();
    }
}
