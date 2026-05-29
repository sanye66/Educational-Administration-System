package com.edu.selection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.common.core.result.R;
import com.edu.selection.entity.SelectionRecord;

public interface SelectionService extends IService<SelectionRecord> {

    /**
     * 选课 - 高并发核心接口
     * 使用 Redis 分布式锁 + Sentinel 限流 + Seata AT 分布式事务
     */
    R<?> selectCourse(Long studentId, Long courseId);

    /**
     * 退课
     */
    R<?> dropCourse(Long studentId, Long courseId);
}
