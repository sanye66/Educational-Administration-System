package com.edu.api.course;

import com.edu.api.course.fallback.CourseFeignFallbackFactory;
import com.edu.common.core.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 课程服务远程调用
 */
@FeignClient(contextId = "courseClient", value = "edu-course",
        fallbackFactory = CourseFeignFallbackFactory.class)
public interface CourseFeignClient {

    @GetMapping("/course/{id}")
    R<?> getCourseById(@PathVariable("id") Long id);

    @GetMapping("/course/{id}/capacity")
    R<?> getCourseCapacity(@PathVariable("id") Long id);
}
