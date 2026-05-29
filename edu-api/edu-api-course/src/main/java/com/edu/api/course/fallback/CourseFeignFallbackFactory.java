package com.edu.api.course.fallback;

import com.edu.common.core.result.R;
import com.edu.common.core.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CourseFeignFallbackFactory implements FallbackFactory<com.edu.api.course.CourseFeignClient> {
    @Override
    public com.edu.api.course.CourseFeignClient create(Throwable cause) {
        log.error("课程服务调用失败: {}", cause.getMessage());
        return new com.edu.api.course.CourseFeignClient() {
            @Override
            public R<?> getCourseById(Long id) {
                return R.failed(ResultCode.FAIL.getCode(), "课程服务不可用: " + cause.getMessage());
            }
            @Override
            public R<?> getCourseCapacity(Long id) {
                return R.failed(ResultCode.FAIL.getCode(), "课程服务不可用: " + cause.getMessage());
            }
        };
    }
}
