package com.edu.api.student.fallback;

import com.edu.common.core.result.R;
import com.edu.common.core.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StudentFeignFallbackFactory implements FallbackFactory<com.edu.api.student.StudentFeignClient> {
    @Override
    public com.edu.api.student.StudentFeignClient create(Throwable cause) {
        log.error("学生服务调用失败: {}", cause.getMessage());
        return new com.edu.api.student.StudentFeignClient() {
            @Override
            public R<?> getStudentById(Long id) {
                return R.failed(ResultCode.FAIL.getCode(), "学生服务不可用: " + cause.getMessage());
            }
            @Override
            public R<?> getStudentByUserId(Long userId) {
                return R.failed(ResultCode.FAIL.getCode(), "学生服务不可用: " + cause.getMessage());
            }
        };
    }
}
