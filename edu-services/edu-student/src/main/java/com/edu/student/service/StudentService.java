package com.edu.student.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.common.core.result.R;
import com.edu.student.entity.Student;

public interface StudentService extends IService<Student> {

    R<?> getStudentById(Long id);

    R<?> getStudentByUserId(Long userId);

    R<?> listStudents();

    R<?> createStudent(Student student);

    R<?> updateStudent(Student student);

    R<?> deleteStudent(Long id);

    R<?> searchStudents(String keyword);
}
