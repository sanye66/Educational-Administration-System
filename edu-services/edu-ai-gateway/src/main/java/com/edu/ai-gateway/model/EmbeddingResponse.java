package com.edu.ai-gateway.model;

import lombok.Data;
import java.util.List;

@Data
public class EmbeddingResponse {
    private String object;
    private List<Embedding> data;
    private String model;
    private Usage usage;
    
    @Data
    public static class Embedding {
        private Integer index;
        private List<Double> embedding;
    }
    
    @Data
    public static class Usage {
        private Integer promptTokens;
        private Integer totalTokens;
    }
}