package com.edu.ai-gateway.service;

import com.edu.ai-gateway.model.AiModelConfig;
import java.util.List;
import java.util.Optional;

public interface ModelConfigService {
    List<AiModelConfig> getAllModels();
    
    Optional<AiModelConfig> getModelById(String modelId);
    
    Optional<AiModelConfig> getModelByName(String modelName);
    
    AiModelConfig addModel(AiModelConfig config);
    
    AiModelConfig updateModel(String modelId, AiModelConfig config);
    
    void deleteModel(String modelId);
    
    boolean isModelEnabled(String modelId);
}