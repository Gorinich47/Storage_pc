package ru.storage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.storage.model.Shedule;
import ru.storage.repo.SheduleRepository;

import java.util.List;

@Service
//@RequiredArgsConstructor
public class SheduleService extends BaseService<Shedule, SheduleRepository> {

    private final SheduleRepository sheduleRepository;

    @Autowired
    SheduleService(SheduleRepository sheduleRepository) {
        super(sheduleRepository);
        this.sheduleRepository = sheduleRepository;
    }

    @Override
    protected Shedule newEntity() {
        return new Shedule();
    }

    @Override
    public List<Shedule> getAll() {
        return sheduleRepository.findAllByOrderByEmployeeAscDateStartAscDateEndAsc();
    }

}
