# AI Gateway 服务文档

## 概述

AI Gateway 是教育管理系统的统一 AI 模型调用接口服务，提供智能对话、文本向量化等功能，支持多种 AI 模型提供商（OpenAI、Azure OpenAI、本地模型等）。

## 功能特性

### 1. 统一 AI 模型调用接口
- 支持多种 AI 模型（GPT-4、GPT-3.5、LLaMA 等）
- 统一的 API 接口，简化前端调用
- 自动模型选择和负载均衡

### 2. 智能路由和负载均衡
- 根据请求类型自动选择最优模型
- 支持多模型提供商切换
- 智能降级和故障转移

### 3. 配额管理和限流
- 用户级别的配额管理
- 实时使用统计和监控
- 灵活的限流策略

### 4. 使用记录和监控
- 完整的调用记录
- Token 使用统计
- 性能指标监控

## 架构设计

```
┌─────────────────────────────────────────────────────────────┐
│                      前端应用                                │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ HTTP/REST
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   AI Gateway (8095)                         │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ 限流过滤器   │  │  配额管理    │  │  模型配置    │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│  ┌──────────────────────────────────────────────────────┐  │
│  │              AI Service (智能路由)                    │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ 智能路由
                              ▼
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
        ▼                     ▼                     ▼
┌───────────────┐    ┌───────────────┐    ┌───────────────┐
│   OpenAI      │    │  Azure OpenAI │    │  Local LLaMA  │
│   API         │    │     API       │    │     API       │
└───────────────┘    └───────────────┘    └───────────────┘
```

## 核心组件

### 1. AiGatewayController
提供 RESTful API 接口：
- `POST /api/ai/chat` - AI 对话接口
- `POST /api/ai/chat/stream` - 流式对话接口
- `POST /api/ai/embeddings` - 文本向量化接口
- `GET /api/ai/models` - 获取所有模型
- `POST /api/ai/models` - 添加新模型
- `GET /api/ai/quota/{userId}/{modelId}` - 查询配额

### 2. AiService
AI 模型调用服务：
- 统一的模型调用接口
- 自动重试和错误处理
- 响应缓存优化

### 3. ModelConfigService
模型配置管理：
- 模型配置的增删改查
- 模型启用/禁用控制
- Redis 缓存优化

### 4. QuotaService
配额管理服务：
- 用户配额检查
- 使用记录统计
- 配额重置功能

### 5. RateLimitFilter
限流过滤器：
- 基于 IP 和用户的限流
- 防止滥用和攻击
- 可配置的限流策略

## API 接口文档

### 1. AI 对话接口

**请求：**
```http
POST /api/ai/chat
Content-Type: application/json
X-User-Id: user123

{
  "model": "gpt-4",
  "messages": [
    {
      "role": "user",
      "content": "你好，请介绍一下自己"
    }
  ],
  "temperature": 0.7,
  "maxTokens": 1000
}
```

**响应：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": "chatcmpl-123",
    "object": "chat.completion",
    "created": 1677652288,
    "model": "GPT-4",
    "choices": [
      {
        "index": 0,
        "message": {
          "role": "assistant",
          "content": "你好！我是 AI 助手..."
        },
        "finishReason": "stop"
      }
    ],
    "usage": {
      "promptTokens": 10,
      "completionTokens": 20,
      "totalTokens": 30
    }
  }
}
```

### 2. 文本向量化接口

**请求：**
```http
POST /api/ai/embeddings
Content-Type: application/json
X-User-Id: user123

{
  "model": "gpt-4",
  "input": "这是一段需要向量化的文本"
}
```

**响应：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "object": "list",
    "data": [
      {
        "index": 0,
        "embedding": [0.0023, -0.0235, 0.1234, ...]
      }
    ],
    "model": "gpt-4",
    "usage": {
      "promptTokens": 5,
      "totalTokens": 5
    }
  }
}
```

### 3. 模型管理接口

**获取所有模型：**
```http
GET /api/ai/models
```

**添加新模型：**
```http
POST /api/ai/models
Content-Type: application/json

{
  "modelId": "custom-model",
  "modelName": "Custom Model",
  "provider": "custom",
  "apiUrl": "https://api.example.com/v1/chat",
  "apiKey": "your-api-key",
  "maxTokens": 4096,
  "temperature": 0.7,
  "enabled": true
}
```

### 4. 配额查询接口

**查询剩余配额：**
```http
GET /api/ai/quota/user123/gpt-4
```

**响应：**
```json
{
  "code": 200,
  "message": "success",
  "data": 950
}
```

## 配置说明

### Nacos 配置 (edu-ai-gateway.yml)

```yaml
spring:
  application:
    name: edu-ai-gateway
  cloud:
    nacos:
      discovery:
        server-addr: ${NACOS_SERVER_ADDR:localhost:8848}
      config:
        server-addr: ${NACOS_SERVER_ADDR:localhost:8848}

ai:
  gateway:
    default-model: gpt-4
    timeout: 30000
    max-retries: 3
    enable-cache: true
    cache-ttl: 3600
```

### Docker 配置

```yaml
ai-gateway:
  build:
    context: ../
    dockerfile: Dockerfile.ai-gateway
  container_name: edu-ai-gateway
  ports:
    - "8095:8095"
  environment:
    NACOS_SERVER_ADDR: nacos:8848
    REDIS_HOST: redis
    REDIS_PORT: 6379
  depends_on:
    - nacos
    - redis
```

## 部署指南

### 1. 本地开发

```bash
# 启动依赖服务
cd docker
docker-compose up -d nacos redis

# 启动 AI Gateway
cd ../edu-services/edu-ai-gateway
mvn spring-boot:run
```

### 2. Docker 部署

```bash
# 构建镜像
docker build -f edu-services/edu-ai-gateway/Dockerfile -t edu-ai-gateway .

# 运行容器
docker run -d \
  --name edu-ai-gateway \
  -p 8095:8095 \
  -e NACOS_SERVER_ADDR=localhost:8848 \
  -e REDIS_HOST=localhost \
  -e REDIS_PORT=6379 \
  edu-ai-gateway
```

### 3. Docker Compose 部署

```bash
cd docker
docker-compose up -d ai-gateway
```

### 4. Kubernetes 部署

```bash
# 应用配置
kubectl apply -f k8s/ai-gateway-deployment.yaml
kubectl apply -f k8s/ai-gateway-service.yaml
```

## 使用示例

### 前端调用示例

```typescript
import axios from 'axios';

const aiGatewayClient = axios.create({
  baseURL: 'http://localhost:8095/api/ai',
  headers: {
    'Content-Type': 'application/json',
    'X-User-Id': 'user123'
  }
});

// AI 对话
async function chatWithAI(message: string) {
  const response = await aiGatewayClient.post('/chat', {
    model: 'gpt-4',
    messages: [
      { role: 'user', content: message }
    ],
    temperature: 0.7,
    maxTokens: 1000
  });
  return response.data.data.choices[0].message.content;
}

// 文本向量化
async function getEmbedding(text: string) {
  const response = await aiGatewayClient.post('/embeddings', {
    model: 'gpt-4',
    input: text
  });
  return response.data.data.data[0].embedding;
}

// 查询配额
async function checkQuota(modelId: string) {
  const response = await aiGatewayClient.get(`/quota/user123/${modelId}`);
  return response.data.data;
}
```

### Java 服务调用示例

```java
@RestController
@RequestMapping("/api/course")
public class CourseController {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @PostMapping("/generate-description")
    public String generateDescription(@RequestBody Course course) {
        String url = "http://edu-ai-gateway/api/ai/chat";
        
        Map<String, Object> request = new HashMap<>();
        request.put("model", "gpt-4");
        request.put("messages", List.of(
            Map.of("role", "user", 
                  "content", "请为课程生成描述：" + course.getName())
        ));
        request.put("temperature", 0.7);
        request.put("maxTokens", 500);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-User-Id", "system");
        
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        
        Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
        List<Map<String, Object>> choices = (List<Map<String, Object>>) data.get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        
        return (String) message.get("content");
    }
}
```

## 监控和日志

### Prometheus 指标

AI Gateway 暴露以下指标：
- `ai_gateway_requests_total` - 总请求数
- `ai_gateway_requests_duration_seconds` - 请求耗时
- `ai_gateway_tokens_total` - Token 使用量
- `ai_gateway_quota_remaining` - 剩余配额

### Grafana 仪表板

访问 http://localhost:3300 查看 AI Gateway 监控仪表板。

### 日志查看

```bash
# Docker 日志
docker logs edu-ai-gateway

# Kubernetes 日志
kubectl logs -f deployment/edu-ai-gateway
```

## 故障排查

### 问题 1：模型调用失败

**症状：** 返回 "Model not found" 错误

**解决方案：**
1. 检查模型是否已配置：`GET /api/ai/models`
2. 确认模型是否启用：检查 `enabled` 字段
3. 验证 API Key 是否正确

### 问题 2：配额超限

**症状：** 返回 "Quota exceeded" 错误

**解决方案：**
1. 查询剩余配额：`GET /api/ai/quota/{userId}/{modelId}`
2. 重置配额：`POST /api/ai/quota/reset/{userId}/{modelId}`
3. 调整配额限制

### 问题 3：限流触发

**症状：** 返回 429 错误

**解决方案：**
1. 降低请求频率
2. 调整限流配置
3. 使用批量接口

## 最佳实践

1. **模型选择**
   - 根据任务复杂度选择合适的模型
   - 简单任务使用 GPT-3.5，复杂任务使用 GPT-4
   - 考虑成本和性能平衡

2. **配额管理**
   - 为不同用户设置不同的配额
   - 定期监控使用情况
   - 及时调整配额策略

3. **错误处理**
   - 实现自动重试机制
   - 设置合理的超时时间
   - 记录详细的错误日志

4. **性能优化**
   - 启用响应缓存
   - 使用流式接口处理长文本
   - 批量处理请求

## 安全建议

1. **API Key 管理**
   - 使用环境变量存储 API Key
   - 定期轮换 API Key
   - 不要在前端暴露 API Key

2. **访问控制**
   - 实施用户认证
   - 使用 HTTPS 传输
   - 限制 IP 访问

3. **数据保护**
   - 不记录敏感信息
   - 加密存储用户数据
   - 遵守数据保护法规

## 扩展开发

### 添加新的 AI 模型提供商

1. 在 `ModelConfigServiceImpl` 中添加新模型配置
2. 在 `AiServiceImpl` 中实现对应的调用逻辑
3. 更新文档和测试用例

### 自定义限流策略

1. 修改 `RateLimitFilter` 的限流逻辑
2. 使用 Redis 实现分布式限流
3. 添加限流指标监控

### 集成新的 AI 功能

1. 在 `AiService` 接口中添加新方法
2. 在 `AiServiceImpl` 中实现具体逻辑
3. 在 `AiGatewayController` 中添加 API 端点

## 相关文档

- [QUICKSTART.md](QUICKSTART.md) - 快速启动指南
- [GRAFANA_SERVICE_DISCOVERY.md](GRAFANA_SERVICE_DISCOVERY.md) - Grafana 服务发现
- [Harness CI/CD 配置](harness/pipelines/build.yml) - CI/CD 流水线配置

## 支持

如有问题，请联系开发团队或提交 Issue。

---

**版本：** 1.0.0  
**最后更新：** 2026-05-26  
**作者：** Educational Administration System Team