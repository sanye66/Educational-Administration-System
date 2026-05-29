package com.edu.evaluation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.common.core.result.R;
import com.edu.evaluation.entity.EvaluationRecord;

public interface EvaluationService extends IService<EvaluationRecord> {

    R<?> getEvaluationById(Long id);

    R<?> listEvaluations();

    R<?> createEvaluation(EvaluationRecord evaluation);

    R<?> updateEvaluation(EvaluationRecord evaluation);

    R<?> deleteEvaluation(Long id);

    R<?> getEvaluationsByStudentId(Long studentId);

    R<?> getEvaluationsByTeacherId(Long teacherId);

    R<?> getEvaluationsByCourseId(Long courseId);
}
