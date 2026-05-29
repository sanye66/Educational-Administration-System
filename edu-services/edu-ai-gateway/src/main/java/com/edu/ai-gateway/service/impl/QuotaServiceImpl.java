package com.edu.ai-gateway.service.impl;

import com.edu.ai-gateway.model.UsageRecord;
import com.edu.ai-gateway.service.QuotaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class QuotaServiceImpl implements QuotaService {
    
    private static final String QUOTA_PREFIX = "ai:quota:";
    private static final String USAGE_PREFIX = "ai:usage:";
    private static final String USER_USAGE_KEY = "ai:user:usage:";
    private static final long QUOTA_TTL = 86400;
    private static final long USAGE_TTL = 604800;
    
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    
    public QuotaServiceImpl(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }
    
    @Override
    public boolean checkQuota(String userId, String modelId) {
        String quotaKey = QUOTA_PREFIX + userId + ":" + modelId;
        Integer remaining = (Integer) redisTemplate.opsForValue().get(quotaKey);
        
        if (remaining == null) {
            remaining = 1000;
            redisTemplate.opsForValue().set(quotaKey, remaining, QUOTA_TTL, TimeUnit.SECONDS);
        }
        
        if (remaining <= 0) {
            log.warn("Quota exceeded for user: {}, model: {}", userId, modelId);
            return false;
        }
        
        redisTemplate.opsForValue().decrement(quotaKey);
        return true;
    }
    
    @Override
    public void recordUsage(UsageRecord record) {
        try {
            String usageKey = USAGE_PREFIX + record.getRequestId();
            redisTemplate.opsForValue().set(usageKey, record, USAGE_TTL, TimeUnit.SECONDS);
            
            String userUsageKey = USER_USAGE_KEY + record.getUserId();
            redisTemplate.opsForList().rightPush(userUsageKey, record);
            redisTemplate.opsForList().trim(userUsageKey, 0, 99);
            redisTemplate.expire(userUsageKey, USAGE_TTL, TimeUnit.SECONDS);
            
            log.debug("Recorded usage: {} tokens for user: {}", record.getTotalTokens(), record.getUserId());
        } catch (Exception e) {
            log.error("Failed to record usage", e);
        }
    }
    
    @Override
    public List<UsageRecord> getUserUsage(String userId, int limit) {
        String userUsageKey = USER_USAGE_KEY + userId;
        List<Object> records = redisTemplate.opsForList().range(userUsageKey, 0, limit - 1);
        
        return records.stream()
                .map(obj -> objectMapper.convertValue(obj, UsageRecord.class))
                .toList();
    }
    
    @Override
    public int getRemainingQuota(String userId, String modelId) {
        String quotaKey = QUOTA_PREFIX + userId + ":" + modelId;
        Integer remaining = (Integer) redisTemplate.opsForValue().get(quotaKey);
        return remaining != null ? remaining : 1000;
    }
    
    @Override
    public void resetQuota(String userId, String modelId) {
        String quotaKey = QUOTA_PREFIX + userId + ":" + modelId;
        redisTemplate.delete(quotaKey);
        log.info("Reset quota for user: {}, model: {}", userId, modelId);
    }
}