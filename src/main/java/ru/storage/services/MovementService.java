package ru.storage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.storage.model.Movement;
import ru.storage.repo.MovementRepository;

@Service
//@RequiredArgsConstructor
public class MovementService extends BaseService<Movement, MovementRepository> {

    //private final MovementRepository movementRepository;

    @Autowired
    MovementService(MovementRepository movementRepository) {
        super(movementRepository);
        //this.movementRepository = movementRepository;
    }

    @Override
    protected Movement newEntity() {
        return new Movement();
    }
}
