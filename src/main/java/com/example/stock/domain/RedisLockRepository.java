package com.example.stock.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@Component
public class RedisLockRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public Boolean lock(Long key){
        return redisTemplate
                .opsForValue()
                //stockId, key,
                .setIfAbsent(generateKey(key), "lock", Duration.ofMillis(1000));
    }

    public Boolean unLock(Long key){
        return redisTemplate.delete(generateKey(key));
    }
    /**
     * @return stockId
     */
    private String generateKey(Long key) {
        return key.toString();
    }
}
