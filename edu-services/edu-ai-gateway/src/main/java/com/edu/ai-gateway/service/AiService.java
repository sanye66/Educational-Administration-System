package com.edu.ai-gateway.service;

import com.edu.ai-gateway.model.ChatRequest;
import com.edu.ai-gateway.model.ChatResponse;
import com.edu.ai-gateway.model.EmbeddingRequest;
import com.edu.ai-gateway.model.EmbeddingResponse;
import reactor.core.publisher.Mono;

public interface AiService {
    Mono<ChatResponse> chat(ChatRequest request, String userId);
    
    Mono<EmbeddingResponse> embedding(EmbeddingRequest request, String userId);
    
    Mono<ChatResponse> streamChat(ChatRequest request, String userId);
}