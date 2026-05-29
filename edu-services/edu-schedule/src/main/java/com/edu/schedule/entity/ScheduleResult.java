package com.edu.schedule.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.edu.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("schedule_result")
public class ScheduleResult extends BaseEntity {

    private Long courseId;
    private Long teacherId;
    private Long classroomId;
    private String academicYear;
    private Integer semester;
    /** 星期几: 1-7 */
    private Integer dayOfWeek;
    /** 开始节次 */
    private Integer startSection;
    /** 持续节次 */
    private Integer durationSection;
    /** 排课状态: DRAFT/CONFIRMED/CANCELLED */
    private String status;
}
