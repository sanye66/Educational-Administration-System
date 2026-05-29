package com.edu.selection.controller;

import com.edu.common.core.result.R;
import com.edu.selection.entity.SelectionRecord;
import com.edu.selection.service.SelectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 选课控制器 - 高并发场景核心接口
 */
@RestController
@RequestMapping("/selection")
@RequiredArgsConstructor
@Tag(name = "选课管理", description = "选课/退课/查询接口")
public class SelectionController {

    private final SelectionService selectionService;

    @PostMapping("/select")
    @Operation(summary = "选课", description = "高并发选课接口，使用Redis分布式锁+Sentinel限流")
    public R<?> selectCourse(@RequestParam Long studentId, @RequestParam Long courseId) {
        return selectionService.selectCourse(studentId, courseId);
    }

    @PostMapping("/drop")
    @Operation(summary = "退课")
    public R<?> dropCourse(@RequestParam Long studentId, @RequestParam Long courseId) {
        return selectionService.dropCourse(studentId, courseId);
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "查询学生选课记录")
    public R<?> getStudentSelections(@PathVariable Long studentId) {
        return R.ok(selectionService.lambdaQuery()
                .eq(SelectionRecord::getStudentId, studentId)
                .eq(SelectionRecord::getStatus, "SELECTED")
                .list());
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "查询课程选课学生")
    public R<?> getCourseSelections(@PathVariable Long courseId) {
        return R.ok(selectionService.lambdaQuery()
                .eq(SelectionRecord::getCourseId, courseId)
                .eq(SelectionRecord::getStatus, "SELECTED")
                .list());
    }

    @GetMapping("/list")
    @Operation(summary = "查询选课记录列表")
    public R<?> listSelections() {
        return R.ok(selectionService.list());
    }
}
