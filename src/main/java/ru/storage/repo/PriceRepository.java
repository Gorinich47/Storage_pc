package ru.storage.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.storage.model.Price;

import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Long> {

    //List<Price> findAllOrderById();
    List<Price> findAllByOrderByBoxAscDateStartAscDateEndAsc();
}
