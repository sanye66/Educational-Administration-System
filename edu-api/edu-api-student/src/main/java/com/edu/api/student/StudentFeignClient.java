package com.edu.api.student;

import com.edu.api.student.fallback.StudentFeignFallbackFactory;
import com.edu.common.core.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 学生服务远程调用
 */
@FeignClient(contextId = "studentClient", value = "edu-student",
        fallbackFactory = StudentFeignFallbackFactory.class)
public interface StudentFeignClient {

    @GetMapping("/student/{id}")
    R<?> getStudentById(@PathVariable("id") Long id);

    @GetMapping("/student/user/{userId}")
    R<?> getStudentByUserId(@PathVariable("userId") Long userId);
}
