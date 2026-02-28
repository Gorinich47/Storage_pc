package ru.storage.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.storage.model.Movement;

public interface MovementRepository extends JpaRepository<Movement, Long> {

}
