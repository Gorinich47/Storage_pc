package ru.storage.utilsForTest;

import org.springframework.data.domain.Page;
import ru.storage.enums.Entrance;
import ru.storage.enums.PostEmployee;
import ru.storage.model.Account;
import ru.storage.model.Box;
import ru.storage.model.Client;
import ru.storage.model.Employee;
import ru.storage.utils.PageUtils;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Класс с тестовым объектами. Лучше один раз создать и использовать
 *
 * @author HGP
 * @version v1
 */
public class TestObjects {

    private final Box box1;
    private final Box box2;
    private final Client client1;
    private final Client client2;
    private final Employee employee1;
    private final Employee employee2;
    private final Account account;
    private final Account account2;
    private final Account accountNew;

    private final List<Box> listBox;
    private final List<Client> listClient;
    private final List<Employee> listEmployee;
    private final List<Account> listAccount;
    private final Page<Box> pageBox;

    public TestObjects() {

        box1 = Box.builder()
                .id(1L)
                .idBox("A100")
                .square(25.0)
                .length(5.0)
                .width(5.0)
                .height(3.0)
                .entrance(Entrance.DOOR)
                .isWarm(true)
                .isElectricity(true)
                .isWater(true)
                //.isFree(true)
                //.price(100.0)
                .build();
        box2 = Box.builder()
                .id(2L)
                .idBox("A200")
                .square(50)
                .length(10)
                .width(5)
                .height(3)
                .entrance(Entrance.DOOR)
                .isWarm(true)
                .isElectricity(true)
                .isWater(true)
                .isFree(true)
                .price(200.0)
                .build();
        listBox = Arrays.asList(box1, box2);
        pageBox = new PageUtils<Box>().createPage(listBox);

        client1 = Client.builder()
                .id(1L)
                .firstName("Иван")
                .lastName("Петров")
                .birthDate(Date.valueOf("1975-01-01"))
                .phoneNumber("+7 (900) 111-22-33")
                .emailAddress("test@mail.ru")
                .address("г. Москва")
                .comment("заметка")
                .build();

        client2 = Client.builder()
                .id(2L)
                .firstName("Василий")
                .lastName("Попов")
                .birthDate(Date.valueOf("2002-12-01"))
                .phoneNumber("+7 (900) 222-33-44")
                .emailAddress("test2@mail.ru")
                .address("г. Москва")
                .comment("заметка")
                .build();

        listClient = Arrays.asList(client1, client2);

        employee1 = Employee.builder()
                .id(1L)
                .postEmployee(PostEmployee.MANAGER)
                .dateStart(Date.valueOf("2026-01-01"))
                .dateEnd(Date.valueOf("2026-12-31"))
                .isFullTime(true)
                .firstName("Сотрудник")
                .lastName("Менеджер")
                .birthDate(Date.valueOf("1975-01-01"))
                .phoneNumber("+7 (900) 333-44-55")
                .emailAddress("test3@mail.ru")
                .address("г. Москва")
                .build();
        employee2 = Employee.builder()
                .id(2L)
                .postEmployee(PostEmployee.WORKER)
                .dateStart(Date.valueOf("2026-01-01"))
                .dateEnd(Date.valueOf("2026-12-31"))
                .isFullTime(true)
                .firstName("Сотрудник")
                .lastName("Работник")
                .birthDate(Date.valueOf("1975-01-01"))
                .phoneNumber("+7 (900) 444-55-66")
                .emailAddress("test4@mail.ru")
                .address("г. Москва")
                .build();

        listEmployee = Arrays.asList(employee1, employee2);

        account = Account.builder()
                .id(1L)
                .date(LocalDateTime.of(2024, 1, 1, 12, 0, 1))
                .dateStart(Date.valueOf("2024-01-01"))
                .dateEnd(Date.valueOf("2024-12-01"))
                .box(listBox)
                .client(client1)
                .employee(employee2)
                .build();

        account2 = Account.builder()
                .id(2L)
                .date(LocalDateTime.of(2025, 1, 1, 12, 0, 1))
                .dateStart(Date.valueOf("2025-01-01"))
                .dateEnd(Date.valueOf("2025-12-01"))
                .box(listBox)
                .client(client2)
                .employee(employee2)
                .build();

        accountNew = Account.builder()
                .box(List.of()) // Пустой список боксов
                .client(new Client())     // Пустой клиент для th:field или th:value
                .employee(new Employee()) // Пустой сотрудник
                .build();

        listAccount = List.of(account, account2);
    }

    public Box getBox1() {
        return box1;
    }

    public Box getBox2() {
        return box2;
    }

    public Client getClient1() {
        return client1;
    }

    public Client getClient2() {
        return client2;
    }

    public Employee getEmployee1() {
        return employee1;
    }

    public Employee getEmployee2() {
        return employee2;
    }

    public Account getAccount() {
        return account;
    }

    public Account getAccount2() {
        return account2;
    }

    public Account getAccountNew() {
        return accountNew;
    }

    public List<Account> getListAccount() {
        return listAccount;
    }
    public List<Box> getListBox() {
        return listBox;
    }

    public List<Client> getListClient() {
        return listClient;
    }

    public List<Employee> getListEmployee() {
        return listEmployee;
    }

    public Page<Box> getPageBox() {
        return pageBox;
    }
}
