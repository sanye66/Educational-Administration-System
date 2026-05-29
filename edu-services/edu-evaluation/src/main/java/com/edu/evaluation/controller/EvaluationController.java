package com.edu.evaluation.controller;

import com.edu.common.core.result.R;
import com.edu.evaluation.entity.EvaluationRecord;
import com.edu.evaluation.service.EvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/evaluation")
@RequiredArgsConstructor
@Tag(name = "评价管理", description = "教学评价接口")
public class EvaluationController {

    private final EvaluationService evaluationService;

    @GetMapping("/{id}")
    @Operation(summary = "查询评价详情")
    public R<?> getEvaluationById(@PathVariable Long id) {
        return evaluationService.getEvaluationById(id);
    }

    @GetMapping("/list")
    @Operation(summary = "评价列表")
    public R<?> listEvaluations() {
        return evaluationService.listEvaluations();
    }

    @PostMapping
    @Operation(summary = "提交评价")
    public R<?> createEvaluation(@RequestBody EvaluationRecord evaluation) {
        return evaluationService.createEvaluation(evaluation);
    }

    @PutMapping
    @Operation(summary = "更新评价")
    public R<?> updateEvaluation(@RequestBody EvaluationRecord evaluation) {
        return evaluationService.updateEvaluation(evaluation);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除评价")
    public R<?> deleteEvaluation(@PathVariable Long id) {
        return evaluationService.deleteEvaluation(id);
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "查询学生评价")
    public R<?> getEvaluationsByStudentId(@PathVariable Long studentId) {
        return evaluationService.getEvaluationsByStudentId(studentId);
    }

    @GetMapping("/teacher/{teacherId}")
    @Operation(summary = "查询教师被评价")
    public R<?> getEvaluationsByTeacherId(@PathVariable Long teacherId) {
        return evaluationService.getEvaluationsByTeacherId(teacherId);
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "查询课程评价")
    public R<?> getEvaluationsByCourseId(@PathVariable Long courseId) {
        return evaluationService.getEvaluationsByCourseId(courseId);
    }
}
