package com.edu.teacher.controller;

import com.edu.common.core.result.R;
import com.edu.teacher.entity.Teacher;
import com.edu.teacher.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
@Tag(name = "教师管理", description = "教师信息 CRUD接口")
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/{id}")
    @Operation(summary = "根据 ID查询教师")
    public R<?> getTeacherById(@PathVariable Long id) {
        return teacherService.getTeacherById(id);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "根据用户 ID查询教师")
    public R<?> getTeacherByUserId(@PathVariable Long userId) {
        return teacherService.getTeacherByUserId(userId);
    }

    @GetMapping("/list")
    @Operation(summary = "查询教师列表")
    public R<?> listTeachers() {
        return teacherService.listTeachers();
    }

    @GetMapping("/search")
    @Operation(summary = "搜索教师")
    public R<?> searchTeachers(@RequestParam(required = false) String keyword) {
        return teacherService.searchTeachers(keyword);
    }

    @PostMapping
    @Operation(summary = "创建教师")
    public R<?> createTeacher(@RequestBody Teacher teacher) {
        return teacherService.createTeacher(teacher);
    }

    @PutMapping
    @Operation(summary = "更新教师")
    public R<?> updateTeacher(@RequestBody Teacher teacher) {
        return teacherService.updateTeacher(teacher);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除教师")
    public R<?> deleteTeacher(@PathVariable Long id) {
        return teacherService.deleteTeacher(id);
    }
}
