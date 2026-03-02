package ru.storage.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import ru.storage.model.Account;
import ru.storage.model.Box;
import ru.storage.model.Client;
import ru.storage.model.Employee;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class GeneralService {

    public static MultyObject MultyObject = new MultyObject();

    public DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    }

    public String format(LocalDateTime date, boolean onlyDate) {
        DateTimeFormatter formatter;
        if (onlyDate) {
            formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        } else {
            formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        }

        return date != null ? formatter.format(date) : "-";
    }

    public String formatDateTimeForInput(LocalDateTime date) {
        if (date == null) return "";
        DateTimeFormatter formatter;
        //DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String rez = date.format(formatter);
        return rez;
    }

    public List<Box> sotrByIdBox(List<Box> list) {
        return list.stream().sorted((o1, o2) -> o1.getIdBox().compareTo(o2.getIdBox())).toList();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MultyObject {
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

}
