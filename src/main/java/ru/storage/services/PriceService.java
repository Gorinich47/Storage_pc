package ru.storage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.storage.model.Price;
import ru.storage.repo.PriceRepository;

import java.sql.Date;
import java.util.List;

@Service
//@RequiredArgsConstructor
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
    @Cacheable(value = "prices", key = "'all_prices'")
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
