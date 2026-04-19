package ru.storage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.storage.interfaces.ClearBaseCache;

import java.util.List;
import java.util.Optional;

public abstract class BaseService<T, R extends JpaRepository<T, Long> & JpaSpecificationExecutor<T>> {

    protected final R repository;
    @Autowired
    private CacheManager cacheManager;

    public BaseService(R repository) {
        this.repository = repository;
    }

    public List<T> getAll() {
        return repository.findAll();
    }

    public Page<T> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<T> getById(Long id) {
        return repository.findById(id);
    }

    public T getByIdOrNew(Long id) {
        return id != null ? repository.findById(id).orElseGet(this::newEntity) : newEntity();
    }

    public List<T> getByListId(List<Long> ids) {
        return repository.findAllById(ids);
    }

    // Новый метод с пагинацией по списку ID
    public Page<T> getByListId(List<Long> ids, Pageable pageable) {

        if (ids == null || ids.isEmpty()) {
            return Page.empty(pageable);
        }

        // Создаем спецификацию "на лету"
        Specification<T> spec = (root, query, cb) -> root.get("id").in(ids);

        return repository.findAll(spec, pageable);
    }

    public List<T> getByListId(Long id) {
        return repository.findAllById(List.of(id));
    }

    // Новый метод с пагинацией по списку ID
    public Page<T> getByListId(Long ids, Pageable pageable) {

        if (ids == null) {
            return Page.empty(pageable);
        }

        // Создаем спецификацию "на лету"
        Specification<T> spec = (root, query, cb) -> root.get("id").in(List.of(ids));

        return repository.findAll(spec, pageable);
    }

    /* Модификация данных требует обновление кешей
       Метод заставит наследников указать имя своего кэша
       По умолчанию буте возвращено значение NULL и кэш очищаться не будет
     */
    public String getCacheName() {
        return null;
    }

    @ClearBaseCache
    public T save(T object) {
        //   clearCache(); // Очищаем после удаления
        return repository.save(object);
    }

    @ClearBaseCache
    public void delete(Long id) {
        repository.deleteById(id);
        //    clearCache(); // Очищаем после удаления
    }


    @ClearBaseCache
    public void delete(T object) {
        repository.delete(object);
        //    clearCache(); // Очищаем после удаления
    }

    protected abstract T newEntity();

    private void clearCache() {
        String name = getCacheName();
        if (name != null) {
            Cache cache = cacheManager.getCache(name);
            if (cache != null) {
                cache.clear(); // Аналог allEntries = true
            }
        }
    }
}