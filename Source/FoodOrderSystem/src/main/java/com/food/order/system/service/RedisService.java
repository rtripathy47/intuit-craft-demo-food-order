package com.food.order.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
	
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    
    private static final String CAPACITY_KEY_PREFIX = "restaurant:capacity:";

    public void setCapacity(Long restaurantId, Integer currentLoad) {
        String key = CAPACITY_KEY_PREFIX + restaurantId;
        redisTemplate.opsForValue().set(key, currentLoad.toString());
    }

    public Integer getCapacity(Long restaurantId) {
        String key = CAPACITY_KEY_PREFIX + restaurantId;
        String value = redisTemplate.opsForValue().get(key);
        return value != null ? Integer.parseInt(value) : 0;
    }

    public void decreaseCapacity(Long restaurantId, int delta) {
        String key = CAPACITY_KEY_PREFIX + restaurantId;
        redisTemplate.opsForValue().decrement(key, delta);
    }
    
    public void increaseCapacity(Long restaurantId, int delta) {
        String key = CAPACITY_KEY_PREFIX + restaurantId;
        redisTemplate.opsForValue().increment(key, delta);
    }
    
    
}
