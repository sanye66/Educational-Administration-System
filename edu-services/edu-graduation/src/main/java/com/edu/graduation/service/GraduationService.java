package com.edu.graduation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.common.core.result.R;
import com.edu.graduation.entity.GraduationTopic;

public interface GraduationService extends IService<GraduationTopic> {

    R<?> getTopicById(Long id);

    R<?> listTopics();

    R<?> createTopic(GraduationTopic topic);

    R<?> updateTopic(GraduationTopic topic);

    R<?> deleteTopic(Long id);

    R<?> selectTopic(Long topicId, Long studentId);

    R<?> getTopicsByTeacherId(Long teacherId);

    R<?> getTopicsByStudentId(Long studentId);
}
