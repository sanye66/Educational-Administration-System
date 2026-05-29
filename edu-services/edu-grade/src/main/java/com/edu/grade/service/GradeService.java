package com.edu.grade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.common.core.result.R;
import com.edu.grade.entity.Grade;

public interface GradeService extends IService<Grade> {

    R<?> getGradeById(Long id);

    R<?> listGrades();

    R<?> createGrade(Grade grade);

    R<?> updateGrade(Grade grade);

    R<?> deleteGrade(Long id);

    R<?> getGradesByStudentId(Long studentId);

    R<?> getGradesByCourseId(Long courseId);

    R<?> batchImportGrades(java.util.List<Grade> grades);
}
