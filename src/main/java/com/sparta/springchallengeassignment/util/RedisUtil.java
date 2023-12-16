package com.sparta.springchallengeassignment.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> void saveKey(String key, Integer minutes, T value) {
        String valueString = null;

        try {
            valueString = !(value instanceof String) ? objectMapper.writeValueAsString(value) : (String) value;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        redisTemplate.opsForValue().set(key, valueString);
        redisTemplate.expire(key, minutes, TimeUnit.MINUTES);
    }

    public String getKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public <T> T getKey(String key, Class<T> valueType) {
        String value = getKey(key);
        if (Objects.isNull(value)) {
            return null;
        }

        try {
            return objectMapper.readValue(value, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteKey(String key) {
        redisTemplate.opsForValue().getOperations().delete(key);
    }

    public void setDataExpire(String value, String key, long duration) {
        Duration expireDuration = Duration.ofSeconds(duration);
        redisTemplate.opsForValue().set(key, value, expireDuration);

    }
}
