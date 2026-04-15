package ru.storage.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.storage.model.Price;

import java.sql.Date;
import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Long>, JpaSpecificationExecutor<Price> {

    String PRICE_FOR_BOX_AND_CURRENT_DATE = """
            SELECT p
            FROM Price p
            WHERE p.box.id IN :boxIds 
                AND :date BETWEEN dateStart and coalesce(dateEnd,'2099-12-31')         
            """;
    //List<Price> findAllOrderById();
    List<Price> findAllByOrderByBoxAscDateStartAscDateEndAsc();

    @Query(PRICE_FOR_BOX_AND_CURRENT_DATE)
    List<Price> findAllByBoxIdIn(@Param("boxIds") List<Long> boxIds,
                                 @Param("date") Date dateNow);

}
