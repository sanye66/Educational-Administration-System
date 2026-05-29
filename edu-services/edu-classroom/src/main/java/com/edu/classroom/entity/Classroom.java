package com.edu.classroom.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.edu.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("classroom")
public class Classroom extends BaseEntity {

    /** 教室编号 */
    private String roomNo;

    /** 教室名称 */
    private String roomName;

    /** 所属建筑 */
    private String building;

    /** 楼层 */
    private Integer floor;

    /** 容量 */
    private Integer capacity;

    /** 教室类型: LECTURE/LAB/COMPUTER/MULTIMEDIA */
    private String roomType;

    /** 设备描述 */
    private String equipment;

    /** 状态: AVAILABLE/OCCUPIED/MAINTENANCE */
    private String status;
}
