package ru.storage.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * Настройка RedisTemplate для ручного управления кэшем (opsForValue и т.д.)
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {

/*       Jackson2JsonRedisSerializer - Deprecated(since = "4.0", forRemoval = true)
        GenericJackson2JsonRedisSerializer - Deprecated(since = "4.0", forRemoval = true)
*/
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.json()); // Актуально для всех типов объектов

        return template;
    }

    /**
     * Настройка конфигурации кэша по умолчанию (используется аннотацией @Cacheable)
     */
    @Bean
    public RedisCacheConfiguration cacheConfiguration() {

        // Вместо создания GenericJackson2JsonRedisSerializer вручную:
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                // Используем новый актуальный метод:
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json())
                );
    }

    /**
     * Индивидуальные настройки TTL для конкретных кэшей (например, "employees")
     */
    @Bean
    public org.springframework.cache.CacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(120))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .withCacheConfiguration("employees", config) // здесь можно задать другой конфиг для конкретного кэша
                .withCacheConfiguration("prices", config) // здесь можно задать другой конфиг для конкретного кэша
                .build();
    }
}