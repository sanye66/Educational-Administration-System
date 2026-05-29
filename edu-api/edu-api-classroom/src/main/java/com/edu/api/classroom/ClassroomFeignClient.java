package com.edu.api.classroom;

import com.edu.api.classroom.fallback.ClassroomFeignFallbackFactory;
import com.edu.common.core.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 教室服务远程调用
 */
@FeignClient(contextId = "classroomClient", value = "edu-classroom",
        fallbackFactory = ClassroomFeignFallbackFactory.class)
public interface ClassroomFeignClient {

    @GetMapping("/classroom/{id}")
    R<?> getClassroomById(@PathVariable("id") Long id);

    @GetMapping("/classroom/{id}/availability")
    R<?> getClassroomAvailability(@PathVariable("id") Long id);
}
