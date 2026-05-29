package com.edu.api.classroom.fallback;

import com.edu.common.core.result.R;
import com.edu.common.core.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClassroomFeignFallbackFactory implements FallbackFactory<com.edu.api.classroom.ClassroomFeignClient> {
    @Override
    public com.edu.api.classroom.ClassroomFeignClient create(Throwable cause) {
        log.error("教室服务调用失败: {}", cause.getMessage());
        return new com.edu.api.classroom.ClassroomFeignClient() {
            @Override
            public R<?> getClassroomById(Long id) {
                return R.failed(ResultCode.FAIL.getCode(), "教室服务不可用: " + cause.getMessage());
            }
            @Override
            public R<?> getClassroomAvailability(Long id) {
                return R.failed(ResultCode.FAIL.getCode(), "教室服务不可用: " + cause.getMessage());
            }
        };
    }
}
