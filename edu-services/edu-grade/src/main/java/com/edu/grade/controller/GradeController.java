package com.edu.grade.controller;

import com.edu.common.core.result.R;
import com.edu.grade.entity.Grade;
import com.edu.grade.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grade")
@RequiredArgsConstructor
@Tag(name = "成绩管理", description = "成绩录入/查询接口")
public class GradeController {

    private final GradeService gradeService;

    @GetMapping("/{id}")
    @Operation(summary = "查询成绩详情")
    public R<?> getGradeById(@PathVariable Long id) {
        return gradeService.getGradeById(id);
    }

    @GetMapping("/list")
    @Operation(summary = "成绩列表")
    public R<?> listGrades() {
        return gradeService.listGrades();
    }

    @PostMapping
    @Operation(summary = "录入成绩")
    public R<?> createGrade(@RequestBody Grade grade) {
        return gradeService.createGrade(grade);
    }

    @PutMapping
    @Operation(summary = "更新成绩")
    public R<?> updateGrade(@RequestBody Grade grade) {
        return gradeService.updateGrade(grade);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除成绩")
    public R<?> deleteGrade(@PathVariable Long id) {
        return gradeService.deleteGrade(id);
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "查询学生成绩")
    public R<?> getStudentGrades(@PathVariable Long studentId) {
        return gradeService.getGradesByStudentId(studentId);
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "查询课程成绩")
    public R<?> getCourseGrades(@PathVariable Long courseId) {
        return gradeService.getGradesByCourseId(courseId);
    }

    @PostMapping("/batch")
    @Operation(summary = "批量导入成绩")
    public R<?> batchImportGrades(@RequestBody List<Grade> grades) {
        return gradeService.batchImportGrades(grades);
    }
}
