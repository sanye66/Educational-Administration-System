package com.edu.classroom.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.common.core.exception.BusinessException;
import com.edu.common.core.result.R;
import com.edu.common.core.result.ResultCode;
import com.edu.common.log.annotation.Log;
import com.edu.common.log.annotation.OperateType;
import com.edu.classroom.entity.Classroom;
import com.edu.classroom.mapper.ClassroomMapper;
import com.edu.classroom.service.ClassroomService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassroomServiceImpl extends ServiceImpl<ClassroomMapper, Classroom> implements ClassroomService {

    @Override
    public R<?> getClassroomById(Long id) {
        Classroom classroom = this.getById(id);
        if (classroom == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "教室不存在");
        }
        return R.ok(classroom);
    }

    @Override
    public R<?> listClassrooms() {
        List<Classroom> list = this.list();
        return R.ok(list);
    }

    @Override
    @Log(value = "创建教室", operateType = OperateType.INSERT)
    public R<?> createClassroom(Classroom classroom) {
        this.save(classroom);
        return R.ok();
    }

    @Override
    @Log(value = "更新教室", operateType = OperateType.UPDATE)
    public R<?> updateClassroom(Classroom classroom) {
        if (classroom.getId() == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR);
        }
        this.updateById(classroom);
        return R.ok();
    }

    @Override
    @Log(value = "删除教室", operateType = OperateType.DELETE)
    public R<?> deleteClassroom(Long id) {
        this.removeById(id);
        return R.ok();
    }

    @Override
    public R<?> searchClassrooms(String keyword) {
        LambdaQueryWrapper<Classroom> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.like(Classroom::getRoomNo, keyword)
                    .or()
                    .like(Classroom::getRoomName, keyword)
                    .or()
                    .like(Classroom::getBuilding, keyword);
        }
        return R.ok(this.list(wrapper));
    }

    @Override
    public R<?> getAvailableClassrooms(Integer capacity, String roomType) {
        LambdaQueryWrapper<Classroom> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Classroom::getStatus, "AVAILABLE");
        if (capacity != null) {
            wrapper.ge(Classroom::getCapacity, capacity);
        }
        if (StrUtil.isNotBlank(roomType)) {
            wrapper.eq(Classroom::getRoomType, roomType);
        }
        return R.ok(this.list(wrapper));
    }
}
