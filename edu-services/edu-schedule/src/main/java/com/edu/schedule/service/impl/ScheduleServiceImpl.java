package com.edu.schedule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.common.core.exception.BusinessException;
import com.edu.common.core.result.R;
import com.edu.common.core.result.ResultCode;
import com.edu.common.log.annotation.Log;
import com.edu.common.log.annotation.OperateType;
import com.edu.schedule.entity.ScheduleResult;
import com.edu.schedule.mapper.ScheduleMapper;
import com.edu.schedule.service.ScheduleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, ScheduleResult> implements ScheduleService {

    @Override
    public R<?> getScheduleById(Long id) {
        ScheduleResult schedule = this.getById(id);
        if (schedule == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "课表不存在");
        }
        return R.ok(schedule);
    }

    @Override
    public R<?> listSchedules() {
        List<ScheduleResult> list = this.list();
        return R.ok(list);
    }

    @Override
    @Log(value = "创建课表", operateType = OperateType.INSERT)
    public R<?> createSchedule(ScheduleResult schedule) {
        this.save(schedule);
        return R.ok();
    }

    @Override
    @Log(value = "更新课表", operateType = OperateType.UPDATE)
    public R<?> updateSchedule(ScheduleResult schedule) {
        if (schedule.getId() == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR);
        }
        this.updateById(schedule);
        return R.ok();
    }

    @Override
    @Log(value = "删除课表", operateType = OperateType.DELETE)
    public R<?> deleteSchedule(Long id) {
        this.removeById(id);
        return R.ok();
    }

    @Override
    public R<?> getSchedulesByCourseId(Long courseId) {
        LambdaQueryWrapper<ScheduleResult> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScheduleResult::getCourseId, courseId);
        return R.ok(this.list(wrapper));
    }

    @Override
    public R<?> getSchedulesByTeacherId(Long teacherId) {
        LambdaQueryWrapper<ScheduleResult> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScheduleResult::getTeacherId, teacherId);
        return R.ok(this.list(wrapper));
    }

    @Override
    public R<?> checkConflict(ScheduleResult schedule) {
        LambdaQueryWrapper<ScheduleResult> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScheduleResult::getDayOfWeek, schedule.getDayOfWeek())
                .eq(ScheduleResult::getClassroomId, schedule.getClassroomId());
        List<ScheduleResult> conflicts = this.list(wrapper);
        
        for (ScheduleResult existing : conflicts) {
            boolean overlap = !(schedule.getStartSection() + schedule.getDurationSection() <= existing.getStartSection()
                    || schedule.getStartSection() >= existing.getStartSection() + existing.getDurationSection());
            if (overlap) {
                throw new BusinessException(ResultCode.SCHEDULE_CONFLICT, "排课时间冲突");
            }
        }
        return R.ok();
    }
}
