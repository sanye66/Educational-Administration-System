package com.edu.api.user.fallback;

import com.edu.common.core.result.R;
import com.edu.common.core.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 用户服务降级工厂
 */
@Slf4j
@Component
public class UserFeignFallbackFactory implements FallbackFactory<com.edu.api.user.UserFeignClient> {

    @Override
    public com.edu.api.user.UserFeignClient create(Throwable cause) {
        log.error("用户服务调用失败: {}", cause.getMessage());
        return new com.edu.api.user.UserFeignClient() {
            @Override
            public R<?> getUserById(Long id) {
                return R.failed(ResultCode.FAIL.getCode(), "用户服务不可用: " + cause.getMessage());
            }

            @Override
            public R<?> getUserByUsername(String username) {
                return R.failed(ResultCode.FAIL.getCode(), "用户服务不可用: " + cause.getMessage());
            }
        };
    }
}
