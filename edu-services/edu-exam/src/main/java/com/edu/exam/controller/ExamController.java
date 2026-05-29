package com.edu.exam.controller;

import com.edu.common.core.result.R;
import com.edu.exam.entity.ExamPlan;
import com.edu.exam.service.ExamPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exam")
@RequiredArgsConstructor
@Tag(name = "考试管理", description = "考试安排接口")
public class ExamController {

    private final ExamPlanService examPlanService;

    @GetMapping("/{id}")
    @Operation(summary = "查询考试详情")
    public R<?> getExamById(@PathVariable Long id) {
        return examPlanService.getExamPlanById(id);
    }

    @GetMapping("/list")
    @Operation(summary = "考试列表")
    public R<?> listExams() {
        return examPlanService.listExamPlans();
    }

    @PostMapping
    @Operation(summary = "创建考试安排")
    public R<?> createExam(@RequestBody ExamPlan examPlan) {
        return examPlanService.createExamPlan(examPlan);
    }

    @PutMapping
    @Operation(summary = "更新考试安排")
    public R<?> updateExam(@RequestBody ExamPlan examPlan) {
        return examPlanService.updateExamPlan(examPlan);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除考试")
    public R<?> deleteExam(@PathVariable Long id) {
        return examPlanService.deleteExamPlan(id);
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "查询课程考试")
    public R<?> getExamsByCourseId(@PathVariable Long courseId) {
        return examPlanService.getExamsByCourseId(courseId);
    }
}
