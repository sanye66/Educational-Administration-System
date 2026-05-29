package com.edu.ai-gateway.controller;

import com.edu.ai-gateway.model.*;
import com.edu.ai-gateway.service.AiService;
import com.edu.ai-gateway.service.ModelConfigService;
import com.edu.ai-gateway.service.QuotaService;
import com.edu.common.core.result.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI Gateway", description = "AI 模型调用接口")
public class AiGatewayController {
    
    private final AiService aiService;
    private final ModelConfigService modelConfigService;
    private final QuotaService quotaService;
    
    @PostMapping("/chat")
    @Operation(summary = "AI 对话接口", description = "调用 AI 模型进行对话")
    public Mono<R<ChatResponse>> chat(
            @Valid @RequestBody ChatRequest request,
            @Parameter(description = "用户ID") @RequestHeader(value = "X-User-Id", defaultValue = "anonymous") String userId) {
        log.info("Chat request from user: {}, model: {}", userId, request.getModel());
        return aiService.chat(request, userId)
                .map(R::ok)
                .onErrorResume(e -> {
                    log.error("Chat request failed", e);
                    return Mono.just(R.error(e.getMessage()));
                });
    }
    
    @PostMapping("/chat/stream")
    @Operation(summary = "AI 流式对话接口", description = "流式调用 AI 模型进行对话")
    public Mono<R<ChatResponse>> streamChat(
            @Valid @RequestBody ChatRequest request,
            @Parameter(description = "用户ID") @RequestHeader(value = "X-User-Id", defaultValue = "anonymous") String userId) {
        log.info("Stream chat request from user: {}, model: {}", userId, request.getModel());
        return aiService.streamChat(request, userId)
                .map(R::ok)
                .onErrorResume(e -> {
                    log.error("Stream chat request failed", e);
                    return Mono.just(R.error(e.getMessage()));
                });
    }
    
    @PostMapping("/embeddings")
    @Operation(summary = "文本向量化接口", description = "将文本转换为向量表示")
    public Mono<R<EmbeddingResponse>> embedding(
            @Valid @RequestBody EmbeddingRequest request,
            @Parameter(description = "用户ID") @RequestHeader(value = "X-User-Id", defaultValue = "anonymous") String userId) {
        log.info("Embedding request from user: {}, model: {}", userId, request.getModel());
        return aiService.embedding(request, userId)
                .map(R::ok)
                .onErrorResume(e -> {
                    log.error("Embedding request failed", e);
                    return Mono.just(R.error(e.getMessage()));
                });
    }
    
    @GetMapping("/models")
    @Operation(summary = "获取所有可用模型", description = "返回所有配置的 AI 模型列表")
    public R<List<AiModelConfig>> getAllModels() {
        List<AiModelConfig> models = modelConfigService.getAllModels();
        return R.ok(models);
    }
    
    @GetMapping("/models/{modelId}")
    @Operation(summary = "获取指定模型信息", description = "根据模型ID获取模型详细信息")
    public R<AiModelConfig> getModelById(@PathVariable String modelId) {
        Optional<AiModelConfig> model = modelConfigService.getModelById(modelId);
        return model.map(R::ok)
                .orElseGet(() -> R.error("Model not found: " + modelId));
    }
    
    @PostMapping("/models")
    @Operation(summary = "添加新模型", description = "添加新的 AI 模型配置")
    public R<AiModelConfig> addModel(@Valid @RequestBody AiModelConfig config) {
        AiModelConfig added = modelConfigService.addModel(config);
        return R.ok(added);
    }
    
    @PutMapping("/models/{modelId}")
    @Operation(summary = "更新模型配置", description = "更新指定模型的配置信息")
    public R<AiModelConfig> updateModel(
            @PathVariable String modelId,
            @Valid @RequestBody AiModelConfig config) {
        AiModelConfig updated = modelConfigService.updateModel(modelId, config);
        return R.ok(updated);
    }
    
    @DeleteMapping("/models/{modelId}")
    @Operation(summary = "删除模型", description = "删除指定的 AI 模型配置")
    public R<Void> deleteModel(@PathVariable String modelId) {
        modelConfigService.deleteModel(modelId);
        return R.ok();
    }
    
    @GetMapping("/quota/{userId}/{modelId}")
    @Operation(summary = "查询剩余配额", description = "查询指定用户在指定模型上的剩余配额")
    public R<Integer> getRemainingQuota(
            @PathVariable String userId,
            @PathVariable String modelId) {
        int remaining = quotaService.getRemainingQuota(userId, modelId);
        return R.ok(remaining);
    }
    
    @GetMapping("/usage/{userId}")
    @Operation(summary = "查询用户使用记录", description = "查询指定用户的使用记录")
    public R<List<UsageRecord>> getUserUsage(
            @PathVariable String userId,
            @Parameter(description = "记录数量限制") @RequestParam(defaultValue = "10") int limit) {
        List<UsageRecord> usage = quotaService.getUserUsage(userId, limit);
        return R.ok(usage);
    }
    
    @PostMapping("/quota/reset/{userId}/{modelId}")
    @Operation(summary = "重置用户配额", description = "重置指定用户在指定模型上的配额")
    public R<Void> resetQuota(
            @PathVariable String userId,
            @PathVariable String modelId) {
        quotaService.resetQuota(userId, modelId);
        return R.ok();
    }
    
    @GetMapping("/health")
    @Operation(summary = "健康检查", description = "检查 AI Gateway 服务状态")
    public R<String> health() {
        return R.ok("AI Gateway is running");
    }
}