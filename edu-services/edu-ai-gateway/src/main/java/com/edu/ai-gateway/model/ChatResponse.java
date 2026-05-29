package com.edu.ai-gateway.model;

import lombok.Data;
import java.util.List;

@Data
public class ChatResponse {
    private String id;
    private String object;
    private Long created;
    private String model;
    private List<Choice> choices;
    private Usage usage;
    private Error error;
    
    @Data
    public static class Choice {
        private Integer index;
        private Message message;
        private String finishReason;
    }
    
    @Data
    public static class Message {
        private String role;
        private String content;
    }
    
    @Data
    public static class Usage {
        private Integer promptTokens;
        private Integer completionTokens;
        private Integer totalTokens;
    }
    
    @Data
    public static class Error {
        private String message;
        private String type;
        private String code;
    }
}