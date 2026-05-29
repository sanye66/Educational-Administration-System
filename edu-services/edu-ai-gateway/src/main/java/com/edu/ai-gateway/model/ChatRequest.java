package com.edu.ai-gateway.model;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
public class ChatRequest {
    @NotBlank(message = "模型名称不能为空")
    private String model;
    
    @NotNull(message = "消息列表不能为空")
    private List<Message> messages;
    
    private Double temperature;
    private Double topP;
    private Integer maxTokens;
    private Boolean stream;
    private Map<String, Object> additionalParams;
    
    @Data
    public static class Message {
        @NotBlank(message = "角色不能为空")
        private String role;
        
        @NotBlank(message = "内容不能为空")
        private String content;
    }
}