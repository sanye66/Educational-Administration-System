package com.edu.api.teacher.fallback;

import com.edu.common.core.result.R;
import com.edu.common.core.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TeacherFeignFallbackFactory implements FallbackFactory<com.edu.api.teacher.TeacherFeignClient> {
    @Override
    public com.edu.api.teacher.TeacherFeignClient create(Throwable cause) {
        log.error("教师服务调用失败: {}", cause.getMessage());
        return new com.edu.api.teacher.TeacherFeignClient() {
            @Override
            public R<?> getTeacherById(Long id) {
                return R.failed(ResultCode.FAIL.getCode(), "教师服务不可用: " + cause.getMessage());
            }
            @Override
            public R<?> getTeacherByUserId(Long userId) {
                return R.failed(ResultCode.FAIL.getCode(), "教师服务不可用: " + cause.getMessage());
            }
        };
    }
}
