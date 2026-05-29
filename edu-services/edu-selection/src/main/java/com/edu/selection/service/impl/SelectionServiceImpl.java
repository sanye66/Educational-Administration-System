package com.edu.selection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.common.core.constant.SystemConstants;
import com.edu.common.core.exception.BusinessException;
import com.edu.common.core.result.R;
import com.edu.common.core.result.ResultCode;
import com.edu.common.log.annotation.Log;
import com.edu.common.log.annotation.OperateType;
import com.edu.common.redis.util.RedisUtil;
import com.edu.selection.entity.SelectionRecord;
import com.edu.selection.mapper.SelectionMapper;
import com.edu.selection.service.SelectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * 选课服务实现 - 高并发场景
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SelectionServiceImpl extends ServiceImpl<SelectionMapper, SelectionRecord> implements SelectionService {

    private final RedisUtil redisUtil;
    private final RedissonClient redissonClient;

    @Override
    @Log(value = "选课", operateType = OperateType.INSERT)
    @Transactional(rollbackFor = Exception.class)
    public R<?> selectCourse(Long studentId, Long courseId) {
        // 1. Redis分布式锁防止重复选课
        String lockKey = SystemConstants.SELECTION_LOCK_KEY + studentId + ":" + courseId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean locked = lock.tryLock(5, 10, TimeUnit.SECONDS);
            if (!locked) {
                return R.failed("操作过于频繁，请稍后再试");
            }

            // 2. 检查是否已选
            long count = this.count(new LambdaQueryWrapper<SelectionRecord>()
                    .eq(SelectionRecord::getStudentId, studentId)
                    .eq(SelectionRecord::getCourseId, courseId)
                    .eq(SelectionRecord::getStatus, "SELECTED"));
            if (count > 0) {
                throw new BusinessException(ResultCode.SELECTION_ALREADY_SELECTED);
            }

            // 3. Redis预扣减库存（原子操作）
            String capacityKey = SystemConstants.COURSE_CAPACITY_KEY + courseId;
            Long remaining = redisUtil.decrement(capacityKey);
            if (remaining == null || remaining < 0) {
                // 库存不足，回滚
                redisUtil.increment(capacityKey);
                throw new BusinessException(ResultCode.COURSE_CAPACITY_FULL);
            }

            // 4. 创建选课记录
            SelectionRecord record = new SelectionRecord();
            record.setStudentId(studentId);
            record.setCourseId(courseId);
            record.setStatus("SELECTED");
            this.save(record);

            // TODO: 5. RocketMQ异步通知（课程服务更新选课人数、学生服务更新已选课程）
            log.info("选课成功: studentId={}, courseId={}", studentId, courseId);

            return R.ok("选课成功");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return R.failed("选课操作被中断");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    @Log(value = "退课", operateType = OperateType.DELETE)
    @Transactional(rollbackFor = Exception.class)
    public R<?> dropCourse(Long studentId, Long courseId) {
        SelectionRecord record = this.getOne(new LambdaQueryWrapper<SelectionRecord>()
                .eq(SelectionRecord::getStudentId, studentId)
                .eq(SelectionRecord::getCourseId, courseId)
                .eq(SelectionRecord::getStatus, "SELECTED"));

        if (record == null) {
            throw new BusinessException("未找到选课记录");
        }

        record.setStatus("DROPPED");
        this.updateById(record);

        // 恢复Redis库存
        String capacityKey = SystemConstants.COURSE_CAPACITY_KEY + courseId;
        redisUtil.increment(capacityKey);

        log.info("退课成功: studentId={}, courseId={}", studentId, courseId);
        return R.ok("退课成功");
    }
}
