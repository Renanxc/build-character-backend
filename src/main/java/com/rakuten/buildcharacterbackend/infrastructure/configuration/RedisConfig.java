package com.rakuten.buildcharacterbackend.infrastructure.configuration;

import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisClientConfigurationBuilder;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password:}")
    private RedisPassword redisPassword;

    @Value("${spring.redis.timeout}")
    private int redisTimeout;

    @Value("${spring.redis.jedis.pool.maxIdle}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.minIdle}")
    private int minIdle;

    @Value("${spring.redis.jedis.pool.maxActive}")
    private int maxActive;

    @Value("${spring.redis.jedis.pool.maxWait}")
    private int maxWait;

    @Bean
    JedisPoolConfig jedisPoolConfig() {
        final JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnCreate(true);
        return jedisPoolConfig;
    }
    
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        final RedisStandaloneConfiguration conn = new RedisStandaloneConfiguration();
        conn.setHostName(redisHost);
        conn.setPort(redisPort);
        if(redisPassword.isPresent())
            conn.setPassword(redisPassword);

        final JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
        jedisClientConfiguration.connectTimeout(Duration.ofMillis(redisTimeout));
        jedisClientConfiguration.usePooling();

        final JedisConnectionFactory jedisConFactory = new JedisConnectionFactory(conn,
                jedisClientConfiguration.build());

        return jedisConFactory;
    }

    @Bean(name = "PrimaryRedis")
    @Primary
    public RedisTemplate<String, String> redisTemplate(final RedisConnectionFactory factory) {
        final ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // redis serialize
        final Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        
        final StringRedisTemplate template = new StringRedisTemplate(factory);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashKeySerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
