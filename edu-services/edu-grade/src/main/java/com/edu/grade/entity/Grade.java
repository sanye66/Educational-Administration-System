package com.edu.grade.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.edu.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("grade")
public class Grade extends BaseEntity {

    private Long studentId;
    private Long courseId;
    private Long teacherId;
    private String academicYear;
    private Integer semester;

    /** 平时成绩 */
    private BigDecimal regularScore;

    /** 期中成绩 */
    private BigDecimal midtermScore;

    /** 期末成绩 */
    private BigDecimal finalScore;

    /** 总评成绩 */
    private BigDecimal totalScore;

    /** 绩点 */
    private BigDecimal gpa;

    /** 成绩状态: DRAFT/PUBLISHED/APPEALED */
    private String status;
}
