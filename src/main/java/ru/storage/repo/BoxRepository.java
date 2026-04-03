package ru.storage.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.storage.model.Box;

import java.util.List;

public interface BoxRepository extends JpaRepository<Box, Long>, JpaSpecificationExecutor<Box> {

    Page<Box> findAllByOrderByIdBoxAsc(Pageable pageable);

    @Query("""
            SELECT b
            FROM Box b
            WHERE CAST(b.id AS string) LIKE %:search%
                OR LOWER(b.idBox) LIKE LOWER(CONCAT('%', :search, '%')) 
            """)
    Page<Box> findBySearchIgnoreCase(@Param("search") String search, Pageable pageable);

    List<Box> findAllByOrderByIdBoxAsc();
}

