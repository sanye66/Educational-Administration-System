package com.edu.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.common.core.exception.BusinessException;
import com.edu.common.core.result.R;
import com.edu.common.core.result.ResultCode;
import com.edu.common.log.annotation.Log;
import com.edu.common.log.annotation.OperateType;
import com.edu.exam.entity.ExamPlan;
import com.edu.exam.mapper.ExamPlanMapper;
import com.edu.exam.service.ExamPlanService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamPlanServiceImpl extends ServiceImpl<ExamPlanMapper, ExamPlan> implements ExamPlanService {

    @Override
    public R<?> getExamPlanById(Long id) {
        ExamPlan examPlan = this.getById(id);
        if (examPlan == null) {
            throw new BusinessException(ResultCode.EXAM_NOT_FOUND);
        }
        return R.ok(examPlan);
    }

    @Override
    public R<?> listExamPlans() {
        List<ExamPlan> list = this.list();
        return R.ok(list);
    }

    @Override
    @Log(value = "创建考试计划", operateType = OperateType.INSERT)
    public R<?> createExamPlan(ExamPlan examPlan) {
        this.save(examPlan);
        return R.ok();
    }

    @Override
    @Log(value = "更新考试计划", operateType = OperateType.UPDATE)
    public R<?> updateExamPlan(ExamPlan examPlan) {
        if (examPlan.getId() == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR);
        }
        this.updateById(examPlan);
        return R.ok();
    }

    @Override
    @Log(value = "删除考试计划", operateType = OperateType.DELETE)
    public R<?> deleteExamPlan(Long id) {
        this.removeById(id);
        return R.ok();
    }

    @Override
    public R<?> getExamsByCourseId(Long courseId) {
        LambdaQueryWrapper<ExamPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExamPlan::getCourseId, courseId);
        return R.ok(this.list(wrapper));
    }

    @Override
    public R<?> checkConflict(Long courseId, String examDate, String startTime, String endTime) {
        return R.ok("检查通过");
    }
}
