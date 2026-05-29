package com.edu.teacher.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.edu.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("teacher")
public class Teacher extends BaseEntity {

    /** 关联用户ID */
    private Long userId;

    /** 教师工号 */
    private String teacherNo;

    /** 所属院系ID */
    private Long departmentId;

    /** 职称: PROFESSOR/ASSOCIATE_PROFESSOR/LECTURER/ASSISTANT */
    private String title;

    /** 学历: DOCTOR/MASTER/BACHELOR */
    private String education;

    /** 研究方向 */
    private String researchDirection;

    /** 入职日期 */
    private String entryDate;
}
