package com.edu.ai-gateway.model;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class EmbeddingRequest {
    @NotBlank(message = "模型名称不能为空")
    private String model;
    
    @NotBlank(message = "输入文本不能为空")
    private String input;
    
    private String encodingFormat;
}