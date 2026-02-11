package com.daroch.event.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class RedisService {

  // RedisTemplate configured to store String keys and String (JSON) values
  private final RedisTemplate<String, String> redisTemplate;

  // Shared ObjectMapper provided by Spring
  private final ObjectMapper objectMapper;


  /**
   * <h>Fetches a value from Redis by key and converts JSON to the given class.
   *
   * @return Optional.empty() if key is missing or deserialization fails.
   */
  public <T> Optional<T> get(String key, Class<T> entityClass) {
    try {
      String json = redisTemplate.opsForValue().get(key);

      // Cache miss
      if (json == null) {
        return Optional.empty();
      }

      // Deserialize JSON → object
      return Optional.of(objectMapper.readValue(json, entityClass));

    } catch (Exception e) {
      // Redis failure should NOT break the request
      log.error("Redis GET failed for key={}", key, e);
      return Optional.empty();
    }
  }

  /** Stores an object in Redis as JSON with a TTL. Failures are logged but do not propagate. */
  public void set(String key, Object value, Duration ttl) {
    try {
      // Serialize object → JSON
      String jsonValue = objectMapper.writeValueAsString(value);

      // Store with TTL
      redisTemplate.opsForValue().set(key, jsonValue, ttl);

    } catch (Exception e) {
      // Redis should be best-effort, not fatal
      log.error("Redis SET failed for key={}", key, e);
    }
  }

  /** Deletes a key from Redis. */
  public void delete(String key) {
    try {
      redisTemplate.delete(key);
    } catch (Exception e) {
      log.error("Redis DELETE failed for key={}", key, e);
    }
  }
}
