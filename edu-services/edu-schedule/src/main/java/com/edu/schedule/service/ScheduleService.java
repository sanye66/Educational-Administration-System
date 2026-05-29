package com.edu.schedule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.common.core.result.R;
import com.edu.schedule.entity.ScheduleResult;

public interface ScheduleService extends IService<ScheduleResult> {

    R<?> getScheduleById(Long id);

    R<?> listSchedules();

    R<?> createSchedule(ScheduleResult schedule);

    R<?> updateSchedule(ScheduleResult schedule);

    R<?> deleteSchedule(Long id);

    R<?> getSchedulesByCourseId(Long courseId);

    R<?> getSchedulesByTeacherId(Long teacherId);

    R<?> checkConflict(ScheduleResult schedule);
}
