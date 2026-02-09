package com.daroch.event.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(properties = {"spring.data.redis.host=localhost", "spring.data.redis.port=6379"})
public class RedisTests {

  @Autowired RedisTemplate redisTemplate;

  @Test
  void testRedis() {
    redisTemplate.opsForValue().set("test", "wkanda for ever");

    Object name = redisTemplate.opsForValue().get("test");

    Assertions.assertEquals("wkanda for ever", name);
  }
}
