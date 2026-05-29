package com.edu.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.common.core.result.R;
import com.edu.exam.entity.ExamPlan;

public interface ExamPlanService extends IService<ExamPlan> {

    R<?> getExamPlanById(Long id);

    R<?> listExamPlans();

    R<?> createExamPlan(ExamPlan examPlan);

    R<?> updateExamPlan(ExamPlan examPlan);

    R<?> deleteExamPlan(Long id);

    R<?> getExamsByCourseId(Long courseId);

    R<?> checkConflict(Long courseId, String examDate, String startTime, String endTime);
}
