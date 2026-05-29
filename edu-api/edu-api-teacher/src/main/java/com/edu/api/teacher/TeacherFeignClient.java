package com.edu.api.teacher;

import com.edu.api.teacher.fallback.TeacherFeignFallbackFactory;
import com.edu.common.core.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 教师服务远程调用
 */
@FeignClient(contextId = "teacherClient", value = "edu-teacher",
        fallbackFactory = TeacherFeignFallbackFactory.class)
public interface TeacherFeignClient {

    @GetMapping("/teacher/{id}")
    R<?> getTeacherById(@PathVariable("id") Long id);

    @GetMapping("/teacher/user/{userId}")
    R<?> getTeacherByUserId(@PathVariable("userId") Long userId);
}
