package com.edu.ai-gateway.service.impl;

import com.edu.ai-gateway.model.AiModelConfig;
import com.edu.ai-gateway.service.ModelConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ModelConfigServiceImpl implements ModelConfigService {
    
    private static final String MODELS_KEY = "ai:models";
    private static final String MODEL_PREFIX = "ai:model:";
    private static final long CACHE_TTL = 3600;
    
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    
    public ModelConfigServiceImpl(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        initializeDefaultModels();
    }
    
    private void initializeDefaultModels() {
        List<AiModelConfig> defaultModels = Arrays.asList(
            createOpenAIModel(),
            createAzureOpenAIModel(),
            createLocalModel()
        );
        
        for (AiModelConfig model : defaultModels) {
            redisTemplate.opsForHash().put(MODELS_KEY, model.getModelId(), model);
        }
        log.info("Initialized {} default AI models", defaultModels.size());
    }
    
    private AiModelConfig createOpenAIModel() {
        AiModelConfig config = new AiModelConfig();
        config.setModelId("gpt-4");
        config.setModelName("GPT-4");
        config.setProvider("openai");
        config.setApiUrl("https://api.openai.com/v1/chat/completions");
        config.setMaxTokens(4096);
        config.setTemperature(0.7);
        config.setTopP(1.0);
        config.setMaxRequestsPerMinute(60);
        config.setMaxTokensPerMinute(90000);
        config.setEnabled(true);
        return config;
    }
    
    private AiModelConfig createAzureOpenAIModel() {
        AiModelConfig config = new AiModelConfig();
        config.setModelId("azure-gpt-4");
        config.setModelName("Azure GPT-4");
        config.setProvider("azure");
        config.setApiUrl("https://your-resource.openai.azure.com/openai/deployments/your-deployment/chat/completions");
        config.setMaxTokens(4096);
        config.setTemperature(0.7);
        config.setTopP(1.0);
        config.setMaxRequestsPerMinute(120);
        config.setMaxTokensPerMinute(180000);
        config.setEnabled(false);
        return config;
    }
    
    private AiModelConfig createLocalModel() {
        AiModelConfig config = new AiModelConfig();
        config.setModelId("local-llama");
        config.setModelName("Local LLaMA");
        config.setProvider("local");
        config.setApiUrl("http://localhost:11434/api/generate");
        config.setMaxTokens(2048);
        config.setTemperature(0.8);
        config.setTopP(0.9);
        config.setMaxRequestsPerMinute(30);
        config.setMaxTokensPerMinute(60000);
        config.setEnabled(false);
        return config;
    }
    
    @Override
    public List<AiModelConfig> getAllModels() {
        Map<Object, Object> models = redisTemplate.opsForHash().entries(MODELS_KEY);
        return models.values().stream()
                .map(obj -> objectMapper.convertValue(obj, AiModelConfig.class))
                .toList();
    }
    
    @Override
    public Optional<AiModelConfig> getModelById(String modelId) {
        Object model = redisTemplate.opsForHash().get(MODELS_KEY, modelId);
        if (model != null) {
            return Optional.of(objectMapper.convertValue(model, AiModelConfig.class));
        }
        return Optional.empty();
    }
    
    @Override
    public Optional<AiModelConfig> getModelByName(String modelName) {
        return getAllModels().stream()
                .filter(model -> model.getModelName().equalsIgnoreCase(modelName))
                .findFirst();
    }
    
    @Override
    public AiModelConfig addModel(AiModelConfig config) {
        if (config.getModelId() == null || config.getModelId().isEmpty()) {
            config.setModelId(UUID.randomUUID().toString());
        }
        redisTemplate.opsForHash().put(MODELS_KEY, config.getModelId(), config);
        log.info("Added new AI model: {}", config.getModelId());
        return config;
    }
    
    @Override
    public AiModelConfig updateModel(String modelId, AiModelConfig config) {
        config.setModelId(modelId);
        redisTemplate.opsForHash().put(MODELS_KEY, modelId, config);
        log.info("Updated AI model: {}", modelId);
        return config;
    }
    
    @Override
    public void deleteModel(String modelId) {
        redisTemplate.opsForHash().delete(MODELS_KEY, modelId);
        log.info("Deleted AI model: {}", modelId);
    }
    
    @Override
    public boolean isModelEnabled(String modelId) {
        return getModelById(modelId)
                .map(AiModelConfig::getEnabled)
                .orElse(false);
    }
}