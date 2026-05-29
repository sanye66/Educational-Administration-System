package com.edu.course.controller;

import com.edu.common.core.result.R;
import com.edu.course.entity.Course;
import com.edu.course.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
@Tag(name = "课程管理", description = "课程信息CRUD接口")
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询课程")
    public R<?> getCourseById(@PathVariable Long id) {
        return R.ok(courseService.getById(id));
    }

    @GetMapping("/{id}/capacity")
    @Operation(summary = "查询课程容量")
    public R<?> getCourseCapacity(@PathVariable Long id) {
        Course course = courseService.getById(id);
        if (course != null) {
            return R.ok(java.util.Map.of("maxCapacity", course.getMaxCapacity(),
                    "currentCount", course.getCurrentCount(),
                    "remaining", course.getMaxCapacity() - course.getCurrentCount()));
        }
        return R.failed("课程不存在");
    }

    @GetMapping("/list")
    @Operation(summary = "查询课程列表")
    public R<?> listCourses() {
        return R.ok(courseService.list());
    }

    @PostMapping
    @Operation(summary = "创建课程")
    public R<?> createCourse(@RequestBody Course course) {
        course.setCurrentCount(0);
        courseService.save(course);
        return R.ok();
    }

    @PutMapping
    @Operation(summary = "更新课程")
    public R<?> updateCourse(@RequestBody Course course) {
        courseService.updateById(course);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除课程")
    public R<?> deleteCourse(@PathVariable Long id) {
        return courseService.deleteCourse(id);
    }
}
