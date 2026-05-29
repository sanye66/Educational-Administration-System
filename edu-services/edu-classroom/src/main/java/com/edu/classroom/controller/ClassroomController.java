package com.edu.classroom.controller;

import com.edu.common.core.result.R;
import com.edu.classroom.entity.Classroom;
import com.edu.classroom.service.ClassroomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/classroom")
@RequiredArgsConstructor
@Tag(name = "教室管理", description = "教室信息管理接口")
public class ClassroomController {

    private final ClassroomService classroomService;

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询教室")
    public R<?> getClassroomById(@PathVariable Long id) {
        return classroomService.getClassroomById(id);
    }

    @GetMapping("/list")
    @Operation(summary = "查询教室列表")
    public R<?> listClassrooms() {
        return classroomService.listClassrooms();
    }

    @GetMapping("/search")
    @Operation(summary = "搜索教室")
    public R<?> searchClassrooms(@RequestParam(required = false) String keyword) {
        return classroomService.searchClassrooms(keyword);
    }

    @GetMapping("/available")
    @Operation(summary = "查询可用教室")
    public R<?> getAvailableClassrooms(
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) String roomType) {
        return classroomService.getAvailableClassrooms(capacity, roomType);
    }

    @PostMapping
    @Operation(summary = "创建教室")
    public R<?> createClassroom(@RequestBody Classroom classroom) {
        return classroomService.createClassroom(classroom);
    }

    @PutMapping
    @Operation(summary = "更新教室")
    public R<?> updateClassroom(@RequestBody Classroom classroom) {
        return classroomService.updateClassroom(classroom);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除教室")
    public R<?> deleteClassroom(@PathVariable Long id) {
        return classroomService.deleteClassroom(id);
    }
}
