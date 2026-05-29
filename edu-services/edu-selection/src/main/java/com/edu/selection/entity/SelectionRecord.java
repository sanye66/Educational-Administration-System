package com.edu.selection.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.edu.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("selection_record")
public class SelectionRecord extends BaseEntity {

    /** 学生ID */
    private Long studentId;

    /** 课程ID */
    private Long courseId;

    /** 选课任务ID */
    private Long taskId;

    /** 学年 */
    private String academicYear;

    /** 学期 */
    private Integer semester;

    /** 选课状态: SELECTED/DROPPED/COMPLETED */
    private String status;
}
