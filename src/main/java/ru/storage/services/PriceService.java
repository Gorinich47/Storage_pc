package ru.storage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.storage.model.Price;
import ru.storage.repo.PriceRepository;

import java.sql.Date;
import java.util.List;

@Service
//@RequiredArgsConstructor
@CacheConfig(cacheNames = "prices") // Теперь BaseService знает, что чистить "prices"
public class PriceService extends BaseService<Price, PriceRepository> {

    private final PriceRepository priceRepository;

    @Autowired
    PriceService(PriceRepository priceRepository) {
        super(priceRepository);
        this.priceRepository = priceRepository;
    }

    @Override
    protected Price newEntity() {
        return new Price();
    }

    @Override
    public String getCacheName() {
        return "prices"; // Включаем очистку кэша для тарифов
    }

    @Override
    @Cacheable(key = "'all_prices'")
    /*@Cacheable(value = "prices", condition = "#result.size() > 10",
            unless = "#result.size() < 1000")*/
    public List<Price> getAll() {
        return priceRepository.findAllByOrderByBoxAscDateStartAscDateEndAsc();
    }

    public List<Price> getAllByBoxId(List<Long> ids, Date date) {
        return priceRepository.findAllByBoxIdIn(ids, date);
    }

    public Double getTotalSum(List<Long> ids, Date date) {
        return priceRepository.findAllByBoxIdInSum(ids, date);
    }
}
