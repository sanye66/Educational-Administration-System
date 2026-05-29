package com.edu.evaluation.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.edu.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("evaluation_record")
public class EvaluationRecord extends BaseEntity {

    private Long studentId;
    private Long courseId;
    private Long teacherId;
    private String academicYear;
    private Integer semester;

    /** 教学态度评分 1-5 */
    private BigDecimal attitudeScore;

    /** 教学内容评分 1-5 */
    private BigDecimal contentScore;

    /** 教学方法评分 1-5 */
    private BigDecimal methodScore;

    /** 教学效果评分 1-5 */
    private BigDecimal effectScore;

    /** 总评评分 */
    private BigDecimal totalScore;

    /** 文字评价 */
    private String comment;

    /** 状态: DRAFT/SUBMITTED */
    private String status;
}
