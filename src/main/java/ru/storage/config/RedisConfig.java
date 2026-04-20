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

/*      Jackson2JsonRedisSerializer - Deprecated(since = "4.0", forRemoval = true)
        GenericJackson2JsonRedisSerializer - Deprecated(since = "4.0", forRemoval = true)
*/

        RedisTemplate<String, Object> template = new RedisTemplate<>(); // Создаем экземпляр шаблона для взаимодействия с Redis
        template.setConnectionFactory(connectionFactory); // Устанавливаем соединение (коннект) с сервером Redis
        template.setKeySerializer(RedisSerializer.string()); // Указываем, что ключи в Redis будут храниться как обычные строки (String)
        template.setValueSerializer(RedisSerializer.json()); // Указываем использовать JSON-сериализатор для значений (автоматически переводит объекты в JSON)

        return template;  // Возвращаем настроенный бин в контекст Spring
    }

    /**
     * Настройка конфигурации кэша по умолчанию (используется аннотацией @Cacheable)
     */
    @Bean
    public RedisCacheConfiguration cacheConfiguration() {

        // Создаем базовую конфигурацию Redis-кэша с настройками "из коробки"
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)) // Устанавливаем "время жизни" кэша — 10 минут
                .disableCachingNullValues() // Запрещаем сохранять в кэш null-значения (защита от пустого кэширования)
                // Настраиваем правила сериализации значений
                .serializeValuesWith(
                        // Используем современный JSON-сериализатор вместо устаревших реализаций
                        RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json())
                );
    }

    /**
     * Индивидуальные настройки TTL для конкретных кэшей (например, "employees")
     */
    @Bean
    public org.springframework.cache.CacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        // Создаем специфическую конфигурацию с TTL 120 минут
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30)) // Время жизни — 30 минут
                .disableCachingNullValues()  // Не кэшируем null
                // Указываем JSON-формат для записи данных в Redis
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));

        // Собираем менеджер кэша, который управляет жизненным циклом всех кэшей в приложении
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config) // Устанавливаем настройки по умолчанию (если кэш не найден в списке ниже)
                .withCacheConfiguration("employees", config) // Применяем настройки конкретно для кэша с именем "employees"
                .withCacheConfiguration("prices", config) // Применяем настройки конкретно для кэша с именем "prices"
                .build(); // билдим сборку
    }
}