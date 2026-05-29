# Feign 客户端模板

本文档提供 Feign 客户端的开发模板，基于接口调用规范生成。

## 1. Feign Client 模板

```java
package com.edu.api.{module};

import com.edu.common.core.domain.R;
import com.edu.api.{module}.fallback.{EntityName}FeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * {业务名称} Feign Client
 *
 * @author author
 */
@FeignClient(
    name = "edu-{module}",
    path = "/api/{module}",
    fallback = {EntityName}FeignClientFallback.class
)
public interface {EntityName}FeignClient {

    /**
     * 根据ID获取{业务名称}
     *
     * @param id ID
     * @return 响应结果
     */
    @GetMapping("/{id}")
    R<{EntityName}VO> getById(@PathVariable("id") Long id);

    /**
     * 获取{业务名称}列表
     *
     * @param queryDTO 查询条件
     * @return 响应结果
     */
    @PostMapping("/list")
    R<List<{EntityName}VO>> list(@RequestBody {EntityName}QueryDTO queryDTO);

    /**
     * 新增{业务名称}
     *
     * @param saveDTO 保存DTO
     * @return 响应结果
     */
    @PostMapping
    R<Long> save(@RequestBody {EntityName}SaveDTO saveDTO);

    /**
     * 修改{业务名称}
     *
     * @param updateDTO 更新DTO
     * @return 响应结果
     */
    @PutMapping
    R<Boolean> update(@RequestBody {EntityName}UpdateDTO updateDTO);

    /**
     * 删除{业务名称}
     *
     * @param id ID
     * @return 响应结果
     */
    @DeleteMapping("/{id}")
    R<Boolean> delete(@PathVariable("id") Long id);
}
```

## 2. Fallback 模板

```java
package com.edu.api.{module}.fallback;

import com.edu.common.core.domain.R;
import com.edu.api.{module}.{EntityName}FeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * {业务名称} Feign Client 降级处理
 *
 * @author author
 */
@Slf4j
@Component
public class {EntityName}FeignClientFallback implements {EntityName}FeignClient {

    @Override
    public R<{EntityName}VO> getById(Long id) {
        log.warn("{EntityName}FeignClient getById 降级处理, id: {}", id);
        return R.failed(new ServiceException(5001, "服务暂时不可用"));
    }

    @Override
    public R<List<{EntityName}VO>> list({EntityName}QueryDTO queryDTO) {
        log.warn("{EntityName}FeignClient list 降级处理");
        return R.failed(new ServiceException(5001, "服务暂时不可用"));
    }

    @Override
    public R<Long> save({EntityName}SaveDTO saveDTO) {
        log.warn("{EntityName}FeignClient save 降级处理");
        return R.failed(new ServiceException(5001, "服务暂时不可用"));
    }

    @Override
    public R<Boolean> update({EntityName}UpdateDTO updateDTO) {
        log.warn("{EntityName}FeignClient update 降级处理");
        return R.failed(new ServiceException(5001, "服务暂时不可用"));
    }

    @Override
    public R<Boolean> delete(Long id) {
        log.warn("{EntityName}FeignClient delete 降级处理, id: {}", id);
        return R.failed(new ServiceException(5001, "服务暂时不可用"));
    }
}
```

## 3. Feign 配置

### 3.1 超时配置 (YAML)

```yaml
feign:
  client:
    config:
      # 默认配置
      default:
        connectTimeout: 5000
        readTimeout: 10000
        loggerLevel: basic
      # 特定服务配置
      edu-user:
        connectTimeout: 3000
        readTimeout: 5000
      edu-course:
        connectTimeout: 3000
        readTimeout: 5000
```

### 3.2 请求拦截器

```java
package com.edu.common.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombokSlf4j;
import org.springframework.stereotype.Component;

/**
 * Feign 请求拦截器 - 传递链路信息
 *
 * @author author
 */
@Slf4j
@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        // 传递 TraceId
        String traceId = MDC.get("traceId");
        if (StringUtils.isNotBlank(traceId)) {
            template.header("X-Trace-Id", traceId);
        }

        // 传递用户信息
        String userId = SecurityUtils.getUserId();
        if (StringUtils.isNotBlank(userId)) {
            template.header("X-User-Id", userId);
        }

        // 传递租户信息
        String tenantId = TenantContextHolder.getTenantId();
        if (StringUtils.isNotBlank(tenantId)) {
            template.header("X-Tenant-Id", tenantId);
        }
    }
}
```

## 4. 使用示例

```java
@Service
@RequiredArgsConstructor
public class {Module}ServiceImpl implements I{Module}Service {

    private final {EntityName}FeignClient {entityName}FeignClient;

    @Override
    public {EntityName}DetailVO get{EntityName}Detail(Long id) {
        // 调用远程服务
        R<{EntityName}VO> result = {entityName}FeignClient.getById(id);

        // 处理响应
        if (result.getCode() != 0) {
            throw new ServiceException(result.getMessage());
        }

        // 转换并返回
        return convertToDetailVO(result.getData());
    }
}
```

## 5. 日志规范

```java
// ✅ 正确：完整的日志记录
log.info("开始调用用户服务获取用户信息, userId: {}, 耗时: {}ms",
         userId, System.currentTimeMillis() - startTime);

// ❌ 错误：缺少关键信息
log.info("获取用户信息");
```

## 6. 错误码处理

| 错误码 | 处理方式 |
|--------|----------|
| 4001 | 用户不存在 |
| 4002 | 用户已存在 |
| 5001 | 服务暂时不可用 |
| 5002 | 服务调用超时 |
