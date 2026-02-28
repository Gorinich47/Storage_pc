package ru.storage.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.storage.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
