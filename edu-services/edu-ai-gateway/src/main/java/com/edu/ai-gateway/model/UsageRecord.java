package com.edu.ai-gateway.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UsageRecord {
    private String userId;
    private String modelId;
    private Integer promptTokens;
    private Integer completionTokens;
    private Integer totalTokens;
    private LocalDateTime requestTime;
    private String requestId;
}