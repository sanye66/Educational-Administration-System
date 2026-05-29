package com.edu.graduation.controller;

import com.edu.common.core.result.R;
import com.edu.graduation.entity.GraduationTopic;
import com.edu.graduation.service.GraduationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/graduation")
@RequiredArgsConstructor
@Tag(name = "毕设管理", description = "毕设题目管理接口")
public class GraduationController {

    private final GraduationService graduationService;

    @GetMapping("/{id}")
    @Operation(summary = "查询题目详情")
    public R<?> getTopicById(@PathVariable Long id) {
        return graduationService.getTopicById(id);
    }

    @GetMapping("/list")
    @Operation(summary = "题目列表")
    public R<?> listTopics() {
        return graduationService.listTopics();
    }

    @PostMapping
    @Operation(summary = "创建题目")
    public R<?> createTopic(@RequestBody GraduationTopic topic) {
        return graduationService.createTopic(topic);
    }

    @PutMapping
    @Operation(summary = "更新题目")
    public R<?> updateTopic(@RequestBody GraduationTopic topic) {
        return graduationService.updateTopic(topic);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除题目")
    public R<?> deleteTopic(@PathVariable Long id) {
        return graduationService.deleteTopic(id);
    }

    @PostMapping("/select/{topicId}")
    @Operation(summary = "选择题目")
    public R<?> selectTopic(@PathVariable Long topicId, @RequestParam Long studentId) {
        return graduationService.selectTopic(topicId, studentId);
    }

    @GetMapping("/teacher/{teacherId}")
    @Operation(summary = "查询教师题目")
    public R<?> getTopicsByTeacherId(@PathVariable Long teacherId) {
        return graduationService.getTopicsByTeacherId(teacherId);
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "查询学生选题")
    public R<?> getTopicsByStudentId(@PathVariable Long studentId) {
        return graduationService.getTopicsByStudentId(studentId);
    }
}
