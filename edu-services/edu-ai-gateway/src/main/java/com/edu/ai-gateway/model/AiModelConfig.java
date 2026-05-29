package com.edu.ai-gateway.model;

import lombok.Data;
import java.util.Map;

@Data
public class AiModelConfig {
    private String modelId;
    private String modelName;
    private String provider;
    private String apiUrl;
    private String apiKey;
    private Integer maxTokens;
    private Double temperature;
    private Double topP;
    private Integer maxRequestsPerMinute;
    private Integer maxTokensPerMinute;
    private Boolean enabled;
    private Map<String, Object> additionalConfig;
}