package ru.storage.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.storage.model.Client;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {

    String SEARCH_QUERY =
            """
                       SELECT t FROM Client t
                         WHERE LOWER(CONCAT(t.firstName, ' ', t.lastName)) LIKE LOWER(CONCAT('%', :search, '%'))
                            OR LOWER(t.emailAddress) LIKE LOWER(CONCAT('%', :search, '%'))
                            OR CAST(FUNCTION('to_char', t.birthDate, 'DD.MM.YYYY') AS string) LIKE CONCAT('%', :search, '%')
                            OR CAST(FUNCTION('regexp_replace', t.phoneNumber, '[^0-9]', '', 'g') AS string) LIKE CONCAT('%', :search, '%')
                    """;


    @Query(value = SEARCH_QUERY, nativeQuery = false)
    Page<Client> findBySearchIgnoreCase(@Param("search") String search, Pageable pageable);

    @Query(value = SEARCH_QUERY, nativeQuery = false)
    List<Client> findBySearchIgnoreCase(@Param("search") String search);
    List<Client> findAllByOrderByFirstNameAscLastNameAsc();

}
