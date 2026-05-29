package com.edu.exam.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.edu.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("exam_plan")
public class ExamPlan extends BaseEntity {

    private Long courseId;
    private Long classroomId;
    private String academicYear;
    private Integer semester;
    private LocalDateTime examTime;
    private Integer duration;
    private String examType; // FINAL/MIDTERM/MAKEUP
    private String status;   // SCHEDULED/IN_PROGRESS/COMPLETED/CANCELLED
}
