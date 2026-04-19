package ru.storage.aspects;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import ru.storage.services.BaseService;

@Aspect
@Component
@RequiredArgsConstructor
public class CacheAspect {

    private final CacheManager cacheManager;

    // Перехватываем выполнение любого метода, помеченного @ClearBaseCache, даже в родителе
    // Аннотация указывает, что метод выполнится ПОСЛЕ успешного завершения целевых методов (без исключений)
    // Pointcut (условие): перехватываем методы save() и все методы, начинающиеся на delete,
    // у любого класса, который наследуется от BaseService (знак + означает иерархию)
    @AfterReturning("execution(* ru.storage.services.BaseService+.save(..)) || " +
            "execution(* ru.storage.services.BaseService+.delete*(..))")
    public void clearCache(JoinPoint joinPoint) {
        // Получаем ссылку на реальный объект (бин сервиса), в котором сработал метод
        Object target = joinPoint.getTarget();

        // Проверяем, является ли объект наследником BaseService (используем Pattern Matching для Java 17+)
        if (target instanceof BaseService<?, ?> baseService) {
            // Вызываем метод сервиса, чтобы узнать имя закрепленного за ним кэша в Redis
            String cacheName = baseService.getCacheName();

            // Если сервис вернул имя кэша (значит, кэширование для этой сущности включено)
            if (cacheName != null) {
                // Пытаемся получить объект управления конкретным кэшем через CacheManager
                var cache = cacheManager.getCache(cacheName);

                // Если такой кэш существует в Redis
                if (cache != null) {
                    // Полностью очищаем все записи в этой категории кэша (аналог allEntries = true)
                    cache.clear();

                    // Технический вывод для проверки работы аспекта в консоли
                    //System.out.println("DEBUG: Aspect triggered for " + cacheName);
                }
            }
        }
    }
}