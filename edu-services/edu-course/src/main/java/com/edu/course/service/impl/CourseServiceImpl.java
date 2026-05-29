package com.edu.course.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.common.core.exception.BusinessException;
import com.edu.common.core.result.R;
import com.edu.common.core.result.ResultCode;
import com.edu.common.log.annotation.Log;
import com.edu.common.log.annotation.OperateType;
import com.edu.course.entity.Course;
import com.edu.course.mapper.CourseMapper;
import com.edu.course.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Override
    public R<?> getCourseById(Long id) {
        Course course = this.getById(id);
        if (course == null) {
            throw new BusinessException(ResultCode.COURSE_NOT_FOUND);
        }
        return R.ok(course);
    }

    @Override
    public R<?> getCourseCapacity(Long id) {
        Course course = this.getById(id);
        if (course == null) {
            throw new BusinessException(ResultCode.COURSE_NOT_FOUND);
        }
        Map<String, Object> capacity = new HashMap<>();
        capacity.put("maxCapacity", course.getMaxCapacity());
        capacity.put("currentCount", course.getCurrentCount());
        capacity.put("remaining", course.getMaxCapacity() - course.getCurrentCount());
        return R.ok(capacity);
    }

    @Override
    public R<?> listCourses() {
        List<Course> list = this.list();
        return R.ok(list);
    }

    @Override
    @Log(value = "创建课程", operateType = OperateType.INSERT)
    public R<?> createCourse(Course course) {
        if (course.getCurrentCount() == null) {
            course.setCurrentCount(0);
        }
        this.save(course);
        return R.ok();
    }

    @Override
    @Log(value = "更新课程", operateType = OperateType.UPDATE)
    public R<?> updateCourse(Course course) {
        if (course.getId() == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR);
        }
        this.updateById(course);
        return R.ok();
    }

    @Override
    @Log(value = "删除课程", operateType = OperateType.DELETE)
    public R<?> deleteCourse(Long id) {
        Course course = this.getById(id);
        if (course == null) {
            throw new BusinessException(ResultCode.COURSE_NOT_FOUND);
        }
        if (course.getCurrentCount() > 0) {
            throw new BusinessException(ResultCode.CONFLICT, "课程已有学生选课，无法删除");
        }
        this.removeById(id);
        return R.ok();
    }

    @Override
    public R<?> searchCourses(String keyword) {
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.like(Course::getCourseNo, keyword)
                    .or()
                    .like(Course::getCourseName, keyword);
        }
        return R.ok(this.list(wrapper));
    }

    @Override
    @Log(value = "增加选课人数", operateType = OperateType.UPDATE)
    public R<?> incrementCount(Long id) {
        Course course = this.getById(id);
        if (course == null) {
            throw new BusinessException(ResultCode.COURSE_NOT_FOUND);
        }
        if (course.getCurrentCount() >= course.getMaxCapacity()) {
            throw new BusinessException(ResultCode.COURSE_CAPACITY_FULL);
        }
        course.setCurrentCount(course.getCurrentCount() + 1);
        this.updateById(course);
        return R.ok();
    }

    @Override
    @Log(value = "减少选课人数", operateType = OperateType.UPDATE)
    public R<?> decrementCount(Long id) {
        Course course = this.getById(id);
        if (course == null) {
            throw new BusinessException(ResultCode.COURSE_NOT_FOUND);
        }
        if (course.getCurrentCount() > 0) {
            course.setCurrentCount(course.getCurrentCount() - 1);
            this.updateById(course);
        }
        return R.ok();
    }
}
