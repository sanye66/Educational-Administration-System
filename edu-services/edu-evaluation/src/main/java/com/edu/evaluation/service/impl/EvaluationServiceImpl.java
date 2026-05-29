package com.edu.evaluation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.common.core.exception.BusinessException;
import com.edu.common.core.result.R;
import com.edu.common.core.result.ResultCode;
import com.edu.common.log.annotation.Log;
import com.edu.common.log.annotation.OperateType;
import com.edu.evaluation.entity.EvaluationRecord;
import com.edu.evaluation.mapper.EvaluationMapper;
import com.edu.evaluation.service.EvaluationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EvaluationServiceImpl extends ServiceImpl<EvaluationMapper, EvaluationRecord> implements EvaluationService {

    @Override
    public R<?> getEvaluationById(Long id) {
        EvaluationRecord evaluation = this.getById(id);
        if (evaluation == null) {
            throw new BusinessException(ResultCode.EVALUATION_NOT_FOUND);
        }
        return R.ok(evaluation);
    }

    @Override
    public R<?> listEvaluations() {
        List<EvaluationRecord> list = this.list();
        return R.ok(list);
    }

    @Override
    @Log(value = "提交评价", operateType = OperateType.INSERT)
    public R<?> createEvaluation(EvaluationRecord evaluation) {
        LambdaQueryWrapper<EvaluationRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EvaluationRecord::getStudentId, evaluation.getStudentId())
                .eq(EvaluationRecord::getCourseId, evaluation.getCourseId());
        if (this.count(wrapper) > 0) {
            throw new BusinessException(ResultCode.EVALUATION_ALREADY_SUBMITTED);
        }
        // 计算总分
        BigDecimal total = evaluation.getAttitudeScore()
                .add(evaluation.getContentScore())
                .add(evaluation.getMethodScore())
                .add(evaluation.getEffectScore())
                .divide(new BigDecimal("4"), 2, BigDecimal.ROUND_HALF_UP);
        evaluation.setTotalScore(total);
        evaluation.setStatus("SUBMITTED");
        this.save(evaluation);
        return R.ok();
    }

    @Override
    @Log(value = "更新评价", operateType = OperateType.UPDATE)
    public R<?> updateEvaluation(EvaluationRecord evaluation) {
        if (evaluation.getId() == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR);
        }
        this.updateById(evaluation);
        return R.ok();
    }

    @Override
    @Log(value = "删除评价", operateType = OperateType.DELETE)
    public R<?> deleteEvaluation(Long id) {
        this.removeById(id);
        return R.ok();
    }

    @Override
    public R<?> getEvaluationsByStudentId(Long studentId) {
        LambdaQueryWrapper<EvaluationRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EvaluationRecord::getStudentId, studentId);
        return R.ok(this.list(wrapper));
    }

    @Override
    public R<?> getEvaluationsByTeacherId(Long teacherId) {
        LambdaQueryWrapper<EvaluationRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EvaluationRecord::getTeacherId, teacherId);
        return R.ok(this.list(wrapper));
    }

    @Override
    public R<?> getEvaluationsByCourseId(Long courseId) {
        LambdaQueryWrapper<EvaluationRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EvaluationRecord::getCourseId, courseId);
        return R.ok(this.list(wrapper));
    }
}
