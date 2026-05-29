package com.edu.course.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.edu.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("course")
public class Course extends BaseEntity {

    /** 课程编号 */
    private String courseNo;

    /** 课程名称 */
    private String courseName;

    /** 课程分类ID */
    private Long categoryId;

    /** 授课教师ID */
    private Long teacherId;

    /** 学分 */
    private BigDecimal credit;

    /** 学时 */
    private Integer classHour;

    /** 课程类型: REQUIRED/ELECTIVE/GENERAL */
    private String courseType;

    /** 最大容量 */
    private Integer maxCapacity;

    /** 当前选课人数 */
    private Integer currentCount;

    /** 学年 */
    private String academicYear;

    /** 学期: 1/2 */
    private Integer semester;

    /** 课程描述 */
    private String description;

    /** 课程状态: DRAFT/PUBLISHED/CLOSED */
    private String status;
}
