# Redis 缓存模板

本文档提供 Redis 缓存的开发模板，基于缓存问题规范生成。

## 1. Redis 配置类

```java
package com.edu.common.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 配置类
 *
 * @author author
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // JSON 序列化配置
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(
            LaissezFaireSubTypeValidator.instance,
            ObjectMapper.DefaultTyping.NON_FINAL
        );
        serializer.setObjectMapper(objectMapper);

        // String 序列化配置
        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        // Key 使用 String 序列化
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // Value 使用 JSON 序列化
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
}
```

## 2. 缓存工具类

```java
package com.edu.common.redis.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis 缓存工具类
 *
 * @author author
 */
@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    // ==================== 缓存操作 ====================

    /**
     * 缓存存值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存存值并设置过期时间
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 缓存取值
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除缓存
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 批量删除
     */
    public Long delete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    // ==================== 分布式锁 ====================

    /**
     * 尝试获取分布式锁
     */
    public Boolean tryLock(String lockKey, String lockValue, long timeout, TimeUnit unit) {
        return redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, timeout, unit);
    }

    /**
     * 释放分布式锁
     */
    public Boolean unlock(String lockKey, String lockValue) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        return redisTemplate.execute(
            new DefaultRedisScript<>(script, Boolean.class),
            Collections.singletonList(lockKey),
            lockValue
        );
    }
}
```

## 3. 缓存查询模板

### 3.1 基本缓存查询

```java
@Service
@RequiredArgsConstructor
public class UserCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserMapper userMapper;

    private static final String CACHE_KEY_PREFIX = "edu:user:info:";
    private static final long CACHE_TIMEOUT = 30;
    private static final TimeUnit CACHE_UNIT = TimeUnit.MINUTES;

    /**
     * 查询用户信息（带缓存）
     */
    public User getUserById(Long userId) {
        String cacheKey = CACHE_KEY_PREFIX + userId;

        // 1. 查询缓存
        User cachedUser = redisTemplate.opsForValue().get(cacheKey);
        if (cachedUser != null) {
            return cachedUser;
        }

        // 2. 查询数据库
        User user = userMapper.selectById(userId);

        // 3. 写入缓存
        if (user != null) {
            redisTemplate.opsForValue().set(cacheKey, user, CACHE_TIMEOUT, CACHE_UNIT);
        }

        return user;
    }

    /**
     * 删除用户缓存
     */
    public void deleteUserCache(Long userId) {
        String cacheKey = CACHE_KEY_PREFIX + userId;
        redisTemplate.delete(cacheKey);
    }
}
```

### 3.2 防缓存击穿模板

```java
/**
 * 查询用户信息（防缓存击穿）
 */
public User getUserById(Long userId) {
    String cacheKey = CACHE_KEY_PREFIX + userId;
    String lockKey = "lock:user:" + userId;

    // 1. 查询缓存
    User user = redisTemplate.opsForValue().get(cacheKey);
    if (user != null) {
        return user;
    }

    // 2. 获取分布式锁
    Boolean acquired = redisTemplate.opsForValue()
        .setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);

    if (Boolean.TRUE.equals(acquired)) {
        try {
            // 3. 双重检查
            user = redisTemplate.opsForValue().get(cacheKey);
            if (user != null) {
                return user;
            }

            // 4. 查询数据库
            user = userMapper.selectById(userId);

            // 5. 写入缓存
            if (user != null) {
                redisTemplate.opsForValue().set(cacheKey, user, CACHE_TIMEOUT, CACHE_UNIT);
            }

            return user;
        } finally {
            // 6. 释放锁
            redisTemplate.delete(lockKey);
        }
    } else {
        // 等待后重试
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return getUserById(userId);
    }
}
```

## 4. 缓存 Key 命名规范

```
格式：{项目}:{模块}:{业务}:{标识}

示例：
edu:user:info:1001          # 用户信息
edu:course:list:teacher:5   # 教师的课程列表
edu:student:cache:grade     # 学生成绩缓存
edu:token:user:1            # 用户Token
edu:lock:course:select      # 选课分布式锁
```

## 5. 缓存过期时间规范

| 缓存类型 | 过期时间 | 说明 |
|----------|----------|------|
| 用户信息 | 30 分钟 | 变化频率低 |
| 列表数据 | 10 分钟 | 变化频率中 |
| 配置数据 | 1 小时 | 变化频率低 |
| 验证码 | 5 分钟 | 短期缓存 |
| Token | 2 小时 | 认证信息 |
| 分布式锁 | 10 秒 | 短期锁 |

## 6. 缓存一致性问题处理

### 6.1 双写模式（更新时）

```java
@Transactional(rollbackFor = Exception.class)
public void updateUser(User user) {
    // 1. 先删除缓存
    redisTemplate.delete(CACHE_KEY_PREFIX + user.getId());

    // 2. 更新数据库
    userMapper.updateById(user);

    // 3. 延迟双删（可选）
    try {
        Thread.sleep(500);
        redisTemplate.delete(CACHE_KEY_PREFIX + user.getId());
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}
```

## 7. 布隆过滤器配置

```java
@Configuration
public class BloomFilterConfig {

    @Bean
    public BloomFilter<String> userBloomFilter() {
        // 预期插入量：100000，误判率：1%
        return BloomFilter.create(
            Funnels.stringFunnel(StandardCharsets.UTF_8),
            100000,
            0.01
        );
    }
}

// 使用
@Service
@RequiredArgsConstructor
public class UserServiceImpl {

    private final BloomFilter<String> userBloomFilter;

    public User getUserById(Long id) {
        // 先检查布隆过滤器
        if (!userBloomFilter.mightContain("user:" + id)) {
            return null;
        }
        // 查询缓存和数据库
    }
}
```
