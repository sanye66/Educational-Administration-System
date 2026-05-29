package com.edu.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.common.core.result.R;
import com.edu.course.entity.Course;

public interface CourseService extends IService<Course> {

    R<?> getCourseById(Long id);

    R<?> getCourseCapacity(Long id);

    R<?> listCourses();

    R<?> createCourse(Course course);

    R<?> updateCourse(Course course);

    R<?> deleteCourse(Long id);

    R<?> searchCourses(String keyword);

    R<?> incrementCount(Long id);

    R<?> decrementCount(Long id);
}
