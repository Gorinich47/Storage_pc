package ru.storage.utilsForTest;

import lombok.Getter;
import org.springframework.data.domain.Page;
import ru.storage.enums.Entrance;
import ru.storage.enums.PostEmployee;
import ru.storage.enums.TypeMovements;
import ru.storage.model.*;
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
@Getter
public class TestObjects {

    private final Box box1;
    private final Box box2;
    private final Client client1;
    private final Client client2;
    private final Client clientNew;
    private final Employee employee1;
    private final Employee employee2;
    private final Employee employeeNew;
    private final Account account;
    private final Account account2;
    private final Account accountNew;
    private final Movement movement1;
    private final Movement movement2;
    private final Movement movementNew;
    private final Price price1;
    private final Price price2;
    private final Price priceNew;
    private final Shedule shedule1;
    private final Shedule shedule2;
    private final Shedule sheduleNew;

    private final List<Box> listBox;
    private final List<Client> listClient;
    private final List<Employee> listEmployee;
    private final List<Account> listAccount;
    private final List<Movement> listMovement;
    private final List<Price> listPrice;
    private final List<Shedule> listShedule;

    private final Page<Box> pageBox;
    private final Page<Client> pageClient;

    public TestObjects() {

        box1 = Box.builder().id(1L).idBox("A100").square(25.0).length(5.0).width(5.0).height(3.0).entrance(Entrance.DOOR)
                .isWarm(true).isElectricity(true).isWater(true).build();
        box2 = Box.builder().id(2L).idBox("A200").square(50.0).length(10.0).width(5.0).height(3.0).entrance(Entrance.DOOR)
                .isWarm(true).isElectricity(true).isWater(true).isFree(true).price(200.0).build();
        listBox = Arrays.asList(box1, box2);
        pageBox = new PageUtils<Box>().createPage(listBox);

        client1 = Client.builder().id(1L).firstName("Иван").lastName("Петров").birthDate(Date.valueOf("1975-01-01"))
                .phoneNumber("+7 (900) 111-22-33").emailAddress("test@mail.ru").address("г. Москва").comment("заметка").build();
        client2 = Client.builder().id(2L).firstName("Василий").lastName("Попов").birthDate(Date.valueOf("2002-12-01"))
                .phoneNumber("+7 (900) 222-33-44").emailAddress("test2@mail.ru").address("г. Москва").comment("заметка").build();
        clientNew = Client.builder().build();
        listClient = Arrays.asList(client1, client2);
        pageClient = new PageUtils<Client>().createPage(listClient);

        employee1 = Employee.builder().id(1L).postEmployee(PostEmployee.MANAGER).dateStart(Date.valueOf("2026-01-01")).dateEnd(Date.valueOf("2026-12-31"))
                .isFullTime(true).firstName("Сотрудник").lastName("Менеджер").birthDate(Date.valueOf("1975-01-01"))
                .phoneNumber("+7 (900) 333-44-55").emailAddress("test3@mail.ru").address("г. Москва").build();
        employee2 = Employee.builder().id(2L).postEmployee(PostEmployee.WORKER).dateStart(Date.valueOf("2026-01-01")).dateEnd(Date.valueOf("2026-12-31"))
                .isFullTime(true).firstName("Сотрудник").lastName("Работник").birthDate(Date.valueOf("1975-01-01"))
                .phoneNumber("+7 (900) 444-55-66").emailAddress("test4@mail.ru").address("г. Москва").build();
        employeeNew = Employee.builder().build();
        listEmployee = Arrays.asList(employee1, employee2);

        account = Account.builder().id(1L).date(LocalDateTime.of(2024, 1, 1, 12, 0, 1))
                .dateStart(Date.valueOf("2024-01-01")).dateEnd(Date.valueOf("2024-12-01")).box(listBox).client(client1).employee(employee2).build();
        account2 = Account.builder().id(2L).date(LocalDateTime.of(2025, 1, 1, 12, 0, 1))
                .dateStart(Date.valueOf("2025-01-01")).dateEnd(Date.valueOf("2025-12-01")).box(listBox).client(client2).employee(employee2).build();
        accountNew = Account.builder().box(List.of()).client(clientNew).employee(employeeNew).build();
        listAccount = List.of(account, account2);

        movement1 = Movement.builder().id(1L).date(LocalDateTime.of(2026, 3, 1, 0, 0, 0))
                .typeMovements(TypeMovements.CREDIT).summ(0.0).account(account).client(client1).employee(employee1).build();
        movement2 = Movement.builder().id(2L).date(LocalDateTime.of(2026, 3, 2, 0, 0, 0))
                .typeMovements(TypeMovements.CREDIT).summ(0.0).account(account).client(client2).employee(employee2).build();
        movementNew = Movement.builder().account(accountNew).client(clientNew).employee(employeeNew).build();
        listMovement = List.of(movement1, movement2);

        price1 = Price.builder().id(1L).box(box1).sumPrice(100.0).dateStart(Date.valueOf("2026-01-01")).dateEnd(Date.valueOf("2026-05-31"))
                .dateEdit(LocalDateTime.of(2026, 3, 31, 20, 0, 0)).build();
        price2 = Price.builder().id(2L).box(box2).sumPrice(100.0).dateStart(Date.valueOf("2026-01-01")).dateEnd(Date.valueOf("2026-05-31"))
                .dateEdit(LocalDateTime.of(2026, 3, 31, 20, 0, 0)).build();
        priceNew = Price.builder().build();
        listPrice = List.of(price1, price2);

        shedule1 = Shedule.builder().id(1L)
                .date(LocalDateTime.of(2026, 3, 31, 7, 0, 0))
                .dateStart(LocalDateTime.of(2026, 3, 31, 8, 30, 0))
                .dateEnd(LocalDateTime.of(2026, 3, 31, 17, 15, 0))
                .employee(employee1).isHoliday(false).build();

        shedule2 = Shedule.builder().id(2L)
                .date(LocalDateTime.of(2026, 3, 31, 7, 0, 0))
                .dateStart(LocalDateTime.of(2026, 3, 31, 8, 30, 0))
                .dateEnd(LocalDateTime.of(2026, 3, 31, 17, 15, 0))
                .employee(employee2).isHoliday(false).build();
        sheduleNew = Shedule.builder().build();
        listShedule = List.of(shedule1, shedule2);
    }

    public Employee getEmployeeNew() {
        return employeeNew;
    }
}
