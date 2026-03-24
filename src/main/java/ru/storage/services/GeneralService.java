package ru.storage.services;

import org.springframework.stereotype.Service;
import ru.storage.model.Box;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author HGP
 * @since 2026-02-20
 * <p>
 * Сервис для общих методов, используемых в Шаблонизаторе
 */
@Service
public class GeneralService {

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


}
