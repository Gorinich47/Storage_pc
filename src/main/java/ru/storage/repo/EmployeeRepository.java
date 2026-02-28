package ru.storage.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.storage.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
