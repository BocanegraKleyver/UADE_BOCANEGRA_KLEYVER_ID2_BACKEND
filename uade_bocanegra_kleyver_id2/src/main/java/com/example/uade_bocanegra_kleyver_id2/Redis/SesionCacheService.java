package com.example.uade_bocanegra_kleyver_id2.Redis;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.uade_bocanegra_kleyver_id2.Entity.Sesion;

@Service
public class SesionCacheService {

    private static final long CACHE_EXPIRATION = 10; // Tiempo de expiración en minutos

    @Autowired
    private RedisTemplate<String, Sesion> redisTemplate;

    public void addToCache(String key, Sesion value) {
        redisTemplate.opsForValue().set(key, value, CACHE_EXPIRATION, TimeUnit.MINUTES);
    }

    public Sesion getFromCache(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public boolean containsKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public void removeFromCache(String key) {
        redisTemplate.delete(key);
    }

    public void expireKey(String key, long expirationTime) {
        redisTemplate.expire(key, expirationTime, TimeUnit.MINUTES);
    }

    public boolean isCacheValid(String key, long expirationTime) {
        Long ttl = redisTemplate.getExpire(key, TimeUnit.MINUTES);
        return Objects.nonNull(ttl) && ttl > expirationTime;
    }
}
