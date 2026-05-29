package com.edu.ai-gateway.service;

import com.edu.ai-gateway.model.UsageRecord;
import java.util.List;

public interface QuotaService {
    boolean checkQuota(String userId, String modelId);
    
    void recordUsage(UsageRecord record);
    
    List<UsageRecord> getUserUsage(String userId, int limit);
    
    int getRemainingQuota(String userId, String modelId);
    
    void resetQuota(String userId, String modelId);
}