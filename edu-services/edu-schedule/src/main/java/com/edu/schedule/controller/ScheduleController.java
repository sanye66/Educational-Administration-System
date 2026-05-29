package com.edu.schedule.controller;

import com.edu.common.core.result.R;
import com.edu.schedule.entity.ScheduleResult;
import com.edu.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
@Tag(name = "排课管理", description = "课表管理接口")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/{id}")
    @Operation(summary = "查询课表详情")
    public R<?> getScheduleById(@PathVariable Long id) {
        return scheduleService.getScheduleById(id);
    }

    @GetMapping("/list")
    @Operation(summary = "课表列表")
    public R<?> listSchedules() {
        return scheduleService.listSchedules();
    }

    @PostMapping
    @Operation(summary = "创建课表")
    public R<?> createSchedule(@RequestBody ScheduleResult schedule) {
        return scheduleService.createSchedule(schedule);
    }

    @PutMapping
    @Operation(summary = "更新课表")
    public R<?> updateSchedule(@RequestBody ScheduleResult schedule) {
        return scheduleService.updateSchedule(schedule);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除课表")
    public R<?> deleteSchedule(@PathVariable Long id) {
        return scheduleService.deleteSchedule(id);
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "查询课程课表")
    public R<?> getSchedulesByCourseId(@PathVariable Long courseId) {
        return scheduleService.getSchedulesByCourseId(courseId);
    }

    @GetMapping("/teacher/{teacherId}")
    @Operation(summary = "查询教师课表")
    public R<?> getSchedulesByTeacherId(@PathVariable Long teacherId) {
        return scheduleService.getSchedulesByTeacherId(teacherId);
    }

    @PostMapping("/check-conflict")
    @Operation(summary = "检查排课冲突")
    public R<?> checkConflict(@RequestBody ScheduleResult schedule) {
        return scheduleService.checkConflict(schedule);
    }
}
