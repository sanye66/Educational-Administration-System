package com.edu.classroom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.common.core.result.R;
import com.edu.classroom.entity.Classroom;

public interface ClassroomService extends IService<Classroom> {

    R<?> getClassroomById(Long id);

    R<?> listClassrooms();

    R<?> createClassroom(Classroom classroom);

    R<?> updateClassroom(Classroom classroom);

    R<?> deleteClassroom(Long id);

    R<?> searchClassrooms(String keyword);

    R<?> getAvailableClassrooms(Integer capacity, String roomType);
}
