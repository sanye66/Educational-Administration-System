package com.edu.graduation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.common.core.exception.BusinessException;
import com.edu.common.core.result.R;
import com.edu.common.core.result.ResultCode;
import com.edu.common.log.annotation.Log;
import com.edu.common.log.annotation.OperateType;
import com.edu.graduation.entity.GraduationTopic;
import com.edu.graduation.mapper.GraduationMapper;
import com.edu.graduation.service.GraduationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GraduationServiceImpl extends ServiceImpl<GraduationMapper, GraduationTopic> implements GraduationService {

    @Override
    public R<?> getTopicById(Long id) {
        GraduationTopic topic = this.getById(id);
        if (topic == null) {
            throw new BusinessException(ResultCode.GRADUATION_TOPIC_NOT_FOUND);
        }
        return R.ok(topic);
    }

    @Override
    public R<?> listTopics() {
        List<GraduationTopic> list = this.list();
        return R.ok(list);
    }

    @Override
    @Log(value = "创建毕设题目", operateType = OperateType.INSERT)
    public R<?> createTopic(GraduationTopic topic) {
        this.save(topic);
        return R.ok();
    }

    @Override
    @Log(value = "更新毕设题目", operateType = OperateType.UPDATE)
    public R<?> updateTopic(GraduationTopic topic) {
        if (topic.getId() == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR);
        }
        this.updateById(topic);
        return R.ok();
    }

    @Override
    @Log(value = "删除毕设题目", operateType = OperateType.DELETE)
    public R<?> deleteTopic(Long id) {
        this.removeById(id);
        return R.ok();
    }

    @Override
    @Log(value = "选择毕设题目", operateType = OperateType.UPDATE)
    public R<?> selectTopic(Long topicId, Long studentId) {
        GraduationTopic topic = this.getById(topicId);
        if (topic == null) {
            throw new BusinessException(ResultCode.GRADUATION_TOPIC_NOT_FOUND);
        }
        if (!"AVAILABLE".equals(topic.getStatus())) {
            throw new BusinessException(ResultCode.GRADUATION_ALREADY_SELECTED);
        }
        topic.setStudentId(studentId);
        topic.setStatus("SELECTED");
        this.updateById(topic);
        return R.ok();
    }

    @Override
    public R<?> getTopicsByTeacherId(Long teacherId) {
        LambdaQueryWrapper<GraduationTopic> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GraduationTopic::getTeacherId, teacherId);
        return R.ok(this.list(wrapper));
    }

    @Override
    public R<?> getTopicsByStudentId(Long studentId) {
        LambdaQueryWrapper<GraduationTopic> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GraduationTopic::getStudentId, studentId);
        return R.ok(this.list(wrapper));
    }
}
