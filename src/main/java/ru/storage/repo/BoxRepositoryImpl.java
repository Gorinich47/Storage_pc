package ru.storage.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import ru.storage.model.Box;

import java.sql.Date;
import java.util.List;

@Component
public class BoxRepositoryImpl implements BoxRepositoryCustom {


    @Query("SELECT b, isNULL(p.price,false) as price, a.id is not null as isFree" +
            "FROM Box b " +
            "left join Price p ON (b.id =p.box_id and :date between date(p.dateStart) AND date(p.endDate))" +
            "left join Account a ON (b.id=a.box_id and :date between date(a.dateStart) AND date(a.endDate))")
    @Override
    public List<Box> findAllNew(@Param("date") Date date) {
        return null;
    }
    // return em.createQuery("SELECT b FROM box",Box.class).getResultList();
}
