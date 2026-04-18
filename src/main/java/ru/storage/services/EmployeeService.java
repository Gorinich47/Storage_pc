package ru.storage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.storage.model.Employee;
import ru.storage.repo.EmployeeRepository;

import java.util.List;

@Service
//@RequiredArgsConstructor
public class EmployeeService extends BaseService<Employee, EmployeeRepository> {

    private final EmployeeRepository employeeRepository;

    @Autowired
    EmployeeService(EmployeeRepository employeeRepository) {
        super(employeeRepository);
        this.employeeRepository = employeeRepository;
    }

    @Override
    protected Employee newEntity() {
        return new Employee();
    }

    @Override
    @Cacheable(value = "employees", key = "'all_employees'")
    public List<Employee> getAll() {
        return employeeRepository.findAllByOrderByLastNameAscFirstNameAscPatronymicAscBirthDateAsc();
    }

}
