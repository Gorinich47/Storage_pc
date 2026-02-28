package ru.storage.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.storage.model.Shedule;

public interface SheduleRepository extends JpaRepository<Shedule, Long> {

}
