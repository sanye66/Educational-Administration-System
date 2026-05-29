package com.edu.graduation.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.edu.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("graduation_topic")
public class GraduationTopic extends BaseEntity {

    private String title;
    private String description;
    private Long teacherId;
    private Long studentId;
    private String academicYear;
    /** 难度: EASY/MEDIUM/HARD */
    private String difficulty;
    /** 状态: AVAILABLE/SELECTED/PROPOSAL/IN_PROGRESS/DEFENSE/DONE */
    private String status;
}
