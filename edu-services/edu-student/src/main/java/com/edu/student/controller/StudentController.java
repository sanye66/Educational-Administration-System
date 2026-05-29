package com.edu.student.controller;

import com.edu.common.core.result.R;
import com.edu.student.entity.Student;
import com.edu.student.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
@Tag(name = "学生管理", description = "学籍与信息管理接口")
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/{id}")
    @Operation(summary = "根据 ID查询学生")
    public R<?> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "根据用户 ID查询学生")
    public R<?> getStudentByUserId(@PathVariable Long userId) {
        return studentService.getStudentByUserId(userId);
    }

    @GetMapping("/list")
    @Operation(summary = "查询学生列表")
    public R<?> listStudents() {
        return studentService.listStudents();
    }

    @GetMapping("/search")
    @Operation(summary = "搜索学生")
    public R<?> searchStudents(@RequestParam(required = false) String keyword) {
        return studentService.searchStudents(keyword);
    }

    @PostMapping
    @Operation(summary = "创建学生")
    public R<?> createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping
    @Operation(summary = "更新学生")
    public R<?> updateStudent(@RequestBody Student student) {
        return studentService.updateStudent(student);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除学生")
    public R<?> deleteStudent(@PathVariable Long id) {
        return studentService.deleteStudent(id);
    }
}
