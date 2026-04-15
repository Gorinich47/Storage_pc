package ru.storage.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import ru.storage.model.Client;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Component
public class DemoUtils {

    @Component
    static public class FIO {

        private static List<String> listNamesMale;
        private static List<String> listNamesFemale;
        private static List<String> listSurnamesMale;
        private static List<String> listSurnamesFemale;
        private static List<String> listPatronymicsMale;
        private static List<String> listPatronymicsFemale;

        private static FioService fioService;
        private static final Random random = new Random();

        // Конструктор для внедрения Spring'ом
        public FIO(FioService fioService) {
            FIO.fioService = fioService;
        }

        static String randomName(int type, boolean isMale) {

            List<String> names = switch (type) {
                case 1 -> (isMale ? listSurnamesMale : listSurnamesFemale);
                case 2 -> (isMale ? listNamesMale : listNamesFemale);
                case 3 -> (isMale ? listPatronymicsMale : listPatronymicsFemale);
                default -> new ArrayList<>();
            };

            return names.get(random.nextInt(names.size() - 1));
        }

        public static Date randomDate() {
            int yearRnd = random.nextInt(1950, 2008);
            int monthRnd = random.nextInt(1, 13);
            int dayRnd = random.nextInt(1, YearMonth.of(yearRnd, monthRnd).lengthOfMonth() + 1);

            return Date.valueOf(LocalDate.of(yearRnd, monthRnd, dayRnd));
        }

        public static String randomEmail(String firstName, String lastName) {

            String[] DOMAINS = {
                    "gmail.com", "yahoo.com", "hotmail.com", "mail.ru",
                    "yandex.ru", "bk.ru", "list.ru", "inbox.ru"
            };

            String domain = DOMAINS[random.nextInt(DOMAINS.length)];
            String separator = random.nextBoolean() ? "." : "_";
            // Транслитерация кириллицы в латиницу

            String latinFirstName = transliterate(firstName);
            String latinLastName = transliterate(lastName);

            String namePart = latinFirstName.toLowerCase(); // + separator + latinLastName.toLowerCase();

            // С вероятностью 20% добавляем случайные цифры
            //if (random.nextDouble() < 0.8) {
            int number = random.nextInt(99999) + 1;
                namePart += number;
            //}

            return String.format("%s@%s", namePart, domain);
        }

        public static Client randomClient() {

            boolean isMale = random.nextBoolean();
            String fam = randomName(1, isMale);
            String im = randomName(2, isMale);
            String ot = randomName(3, isMale);
            Date dateOfBirth = randomDate();
            String phoneNumber = randomPhoneNumber();
            String Email = randomEmail(fam, im);
            String Address = randomAddress();

            Client client = Client.builder()
                    .lastName(fam)
                    .firstName(im)
                    .patronymic(ot)
                    .birthDate(dateOfBirth)
                    .phoneNumber(phoneNumber)
                    .emailAddress(Email)
                    .address(Address)
                    .comment("")
                    .build();

            return client;

        }

        @PostConstruct
        public void init() {
            // 1. Загружаем базовые списки имен и аамилии
            listNamesMale = fioService.loadFioList("male_names_rus");
            listNamesFemale = fioService.loadFioList("female_names_rus");
            listSurnamesMale = fioService.loadFioList("male_surnames_rus");

            // 2. Генерируем фамилии и отчества один раз при старте
            listSurnamesFemale = generateFemaleSurnames(listSurnamesMale);

            listPatronymicsMale = generatePatronymics(listNamesMale, true);
            listPatronymicsFemale = generatePatronymics(listNamesMale, false);
        }

        public static String randomPhoneNumber() {

            return String.format("+7 (9%d%d) %d-%d-%d",
                    random.nextInt(10), random.nextInt(10),
                    random.nextInt(900) + 100,
                    random.nextInt(90) + 10,
                    random.nextInt(90) + 10);
        }

        public static String randomAddress() {
            String[] streets = {
                    "Ленина", "Гагарина", "Пушкина", "Лермонтова", "Толстого",
                    "Чехова", "Достоевского", "Тургенева", "Горького", "Маяковского",
                    "Жукова", "Кутузова", "Суворова", "Бауманская", "Новослободская",
                    "Профсоюзная", "Варшавское", "Ленинградский", "Юдина", "Кима",
                    "Комсомольский", "Щелковское", "Новая", "Первая", "Универсальная",
                    "Центральная", "Крестьянская", "Загородная", "Зеленая", "Петра первого",
                    "Юбилейная", "Советская", "Кирова", "Шоссейная", "Болотная", "Грибоедова",
                    "Невский", "Смоленская", "Рязанская", "Новгородская", "Ярославская",
                    "Грибной", "Большое Село"
            };
            String[] types = {
                    "ул.", "пер.", "пр-кт", "ш.", "б-р."
            };

            String streetType = types[random.nextInt(types.length)];
            String streetName = streets[random.nextInt(streets.length)];
            int houseNumber = random.nextInt(200) + 1;

            return String.format("г. Москва, %s %s, д. %d",
                    streetType, streetName, houseNumber);

        }

        private List<String> generateFemaleSurnames(List<String> maleSurnames) {
            return maleSurnames.stream().map(fam -> {
                if (fam.endsWith("ый") || fam.endsWith("ий")) return fam.substring(0, fam.length() - 2).concat("ая");
                if (fam.matches(".*(ов|ев|ин|ын)$")) return fam.concat("а");
                return fam;
            }).toList();
        }

        public static String transliterate(String cyrillic) {
            Map<Character, String> map = new HashMap<>();
            map.put('а', "a");
            map.put('б', "b");
            map.put('в', "v");
            map.put('г', "g");
            map.put('д', "d");
            map.put('е', "e");
            map.put('ё', "yo");
            map.put('ж', "zh");
            map.put('з', "z");
            map.put('и', "i");
            map.put('й', "y");
            map.put('к', "k");
            map.put('л', "l");
            map.put('м', "m");
            map.put('н', "n");
            map.put('о', "o");
            map.put('п', "p");
            map.put('р', "r");
            map.put('с', "s");
            map.put('т', "t");
            map.put('у', "u");
            map.put('ф', "f");
            map.put('х', "h");
            map.put('ц', "ts");
            map.put('ч', "ch");
            map.put('ш', "sh");
            map.put('щ', "sch");
            map.put('ъ', "");
            map.put('ы', "y");
            map.put('ь', "");
            map.put('э', "e");
            map.put('ю', "yu");
            map.put('я', "ya");

            StringBuilder result = new StringBuilder();
            for (char c : cyrillic.toLowerCase().toCharArray()) {
                if (map.containsKey(c)) {
                    result.append(map.get(c));
                } else if (Character.isLetterOrDigit(c)) {
                    result.append(c);
                }
            }
            return result.toString();
        }

        private List<String> generatePatronymics(List<String> maleNames, boolean isMale) {
            return maleNames.stream().map(name -> {
                if (name.endsWith("ий")) return name.substring(0, name.length() - 2).concat(isMale ? "ьевич" : "ьевна");
                if (name.endsWith("й")) return name.substring(0, name.length() - 1).concat(isMale ? "евич" : "евна");
                return name.concat(isMale ? "ович" : "овна");
            }).toList();
        }
    }
}
