package ru.storage.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.storage.model.Shedule;

import java.util.List;

public interface SheduleRepository extends JpaRepository<Shedule, Long>, JpaSpecificationExecutor<Shedule> {

    List<Shedule> findAllByOrderByEmployeeAscDateStartAscDateEndAsc();
}
