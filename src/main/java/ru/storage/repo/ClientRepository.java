package ru.storage.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.storage.model.Client;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findAllByOrderByFirstNameAscLastNameAsc();
}
