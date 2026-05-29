package com.edu.course.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.course.entity.Course;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface CourseMapper extends BaseMapper<Course> {}
