package ru.storage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.storage.model.Account;
import ru.storage.model.Box;
import ru.storage.model.Client;
import ru.storage.model.Employee;

import java.util.List;

/**
 * @author HGP
 * @since 2026-03-24
 * <p>
 * DTO для передачи нескольких объектов в одном объекте. Пока отказался
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultyObject {
    private Account account;
    private Client client;
    private Employee employee;
    private Box box;
    private List<Account> allAccounts;
    private List<Client> allClients;
    private List<Employee> allEmployees;
    private List<Box> allBoxes;
    private List<Box> boxes;
}