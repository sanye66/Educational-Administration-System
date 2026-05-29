package com.edu.api.user;

import com.edu.api.user.fallback.UserFeignFallbackFactory;
import com.edu.common.core.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 用户服务远程调用
 */
@FeignClient(contextId = "userClient", value = "edu-user",
        fallbackFactory = UserFeignFallbackFactory.class)
public interface UserFeignClient {

    @GetMapping("/user/{id}")
    R<?> getUserById(@PathVariable("id") Long id);

    @GetMapping("/user/username/{username}")
    R<?> getUserByUsername(@PathVariable("username") String username);
}
