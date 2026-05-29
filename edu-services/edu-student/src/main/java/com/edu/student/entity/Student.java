package com.edu.student.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.edu.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("student")
public class Student extends BaseEntity {

    /** 关联用户ID */
    private Long userId;

    /** 学号 */
    private String studentNo;

    /** 所属院系ID */
    private Long departmentId;

    /** 专业ID */
    private Long majorId;

    /** 班级ID */
    private Long classId;

    /** 入学年份 */
    private String enrollmentYear;

    /** 学制年限 */
    private Integer schoolingLength;

    /** 学籍状态: ACTIVE/SUSPENDED/GRADUATED/DROPPED */
    private String status;
}
