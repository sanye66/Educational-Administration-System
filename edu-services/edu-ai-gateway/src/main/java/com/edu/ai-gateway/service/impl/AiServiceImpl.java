package com.edu.ai-gateway.service.impl;

import com.edu.ai-gateway.model.*;
import com.edu.ai-gateway.service.AiService;
import com.edu.ai-gateway.service.ModelConfigService;
import com.edu.ai-gateway.service.QuotaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class AiServiceImpl implements AiService {
    
    private final ModelConfigService modelConfigService;
    private final QuotaService quotaService;
    private final WebClient.Builder webClientBuilder;
    
    public AiServiceImpl(ModelConfigService modelConfigService, 
                        QuotaService quotaService,
                        WebClient.Builder webClientBuilder) {
        this.modelConfigService = modelConfigService;
        this.quotaService = quotaService;
        this.webClientBuilder = webClientBuilder;
    }
    
    @Override
    public Mono<ChatResponse> chat(ChatRequest request, String userId) {
        String requestId = UUID.randomUUID().toString();
        
        return modelConfigService.getModelById(request.getModel())
                .map(Mono::just)
                .orElse(Mono.error(new RuntimeException("Model not found: " + request.getModel())))
                .flatMap(config -> {
                    if (!config.getEnabled()) {
                        return Mono.error(new RuntimeException("Model is disabled: " + request.getModel()));
                    }
                    
                    if (!quotaService.checkQuota(userId, request.getModel())) {
                        return Mono.error(new RuntimeException("Quota exceeded for user: " + userId));
                    }
                    
                    return callOpenAIChat(config, request, requestId);
                })
                .doOnSuccess(response -> recordUsage(request, userId, requestId, response))
                .doOnError(error -> log.error("Chat request failed: {}", error.getMessage()));
    }
    
    @Override
    public Mono<EmbeddingResponse> embedding(EmbeddingRequest request, String userId) {
        String requestId = UUID.randomUUID().toString();
        
        return modelConfigService.getModelById(request.getModel())
                .map(Mono::just)
                .orElse(Mono.error(new RuntimeException("Model not found: " + request.getModel())))
                .flatMap(config -> {
                    if (!config.getEnabled()) {
                        return Mono.error(new RuntimeException("Model is disabled: " + request.getModel()));
                    }
                    
                    if (!quotaService.checkQuota(userId, request.getModel())) {
                        return Mono.error(new RuntimeException("Quota exceeded for user: " + userId));
                    }
                    
                    return callOpenAIEmbedding(config, request, requestId);
                })
                .doOnSuccess(response -> recordEmbeddingUsage(request, userId, requestId, response))
                .doOnError(error -> log.error("Embedding request failed: {}", error.getMessage()));
    }
    
    @Override
    public Mono<ChatResponse> streamChat(ChatRequest request, String userId) {
        request.setStream(true);
        return chat(request, userId);
    }
    
    private Mono<ChatResponse> callOpenAIChat(AiModelConfig config, ChatRequest request, String requestId) {
        WebClient webClient = webClientBuilder
                .baseUrl(config.getApiUrl())
                .defaultHeader("Authorization", "Bearer " + config.getApiKey())
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
        
        return webClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .map(response -> {
                    response.setId(requestId);
                    response.setModel(config.getModelName());
                    response.setCreated(System.currentTimeMillis() / 1000);
                    return response;
                });
    }
    
    private Mono<EmbeddingResponse> callOpenAIEmbedding(AiModelConfig config, EmbeddingRequest request, String requestId) {
        String embeddingUrl = config.getApiUrl().replace("/chat/completions", "/embeddings");
        
        WebClient webClient = webClientBuilder
                .baseUrl(embeddingUrl)
                .defaultHeader("Authorization", "Bearer " + config.getApiKey())
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
        
        return webClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(EmbeddingResponse.class);
    }
    
    private void recordUsage(ChatRequest request, String userId, String requestId, ChatResponse response) {
        if (response.getUsage() != null) {
            UsageRecord record = new UsageRecord();
            record.setUserId(userId);
            record.setModelId(request.getModel());
            record.setPromptTokens(response.getUsage().getPromptTokens());
            record.setCompletionTokens(response.getUsage().getCompletionTokens());
            record.setTotalTokens(response.getUsage().getTotalTokens());
            record.setRequestTime(LocalDateTime.now());
            record.setRequestId(requestId);
            quotaService.recordUsage(record);
        }
    }
    
    private void recordEmbeddingUsage(EmbeddingRequest request, String userId, String requestId, EmbeddingResponse response) {
        if (response.getUsage() != null) {
            UsageRecord record = new UsageRecord();
            record.setUserId(userId);
            record.setModelId(request.getModel());
            record.setPromptTokens(response.getUsage().getPromptTokens());
            record.setCompletionTokens(0);
            record.setTotalTokens(response.getUsage().getTotalTokens());
            record.setRequestTime(LocalDateTime.now());
            record.setRequestId(requestId);
            quotaService.recordUsage(record);
        }
    }
}