package com.edu.teacher.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.common.core.result.R;
import com.edu.teacher.entity.Teacher;

public interface TeacherService extends IService<Teacher> {

    R<?> getTeacherById(Long id);

    R<?> getTeacherByUserId(Long userId);

    R<?> listTeachers();

    R<?> createTeacher(Teacher teacher);

    R<?> updateTeacher(Teacher teacher);

    R<?> deleteTeacher(Long id);

    R<?> searchTeachers(String keyword);
}
