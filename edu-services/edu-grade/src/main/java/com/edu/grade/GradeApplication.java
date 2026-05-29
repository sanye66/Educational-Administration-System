package com.edu.grade;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.edu.api")
@MapperScan("com.edu.grade.mapper")
public class GradeApplication {
    public static void main(String[] args) { 
        SpringApplication.run(GradeApplication.class, args); 
    }
}
