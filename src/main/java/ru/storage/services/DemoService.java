package ru.storage.services;

import org.springframework.stereotype.Service;
import ru.storage.model.Client;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class DemoService {

    static public class FIO {

        static Random random = new Random();

        static String[] getArrayNameMale() {

            String[] names = {"Андрей", "Алексей", "Александр", "Антон", "Анатолий", "Арсений",
                    "Борис", "Богдан",
                    "Валерий", "Вальтер", "Вислав", "Виктор", "Владимир", "Владислав",
                    "Григорий", "Георгий", "Геннадий",
                    "Дмитрий", "Денис", "Добрыня",
                    "Евгений", "Егор", "Ермолай", "Еремей",
                    "Жак",
                    "Захар",
                    "Илья",
                    "Карп", "Кирилл", "Константин", "Клим",
                    "Лев", "Леонид",
                    "Максим", "Михаил", "Марк",
                    "Нестер", "Николай", "Никифор", "Никодим", "Назар", "Никита",
                    "Олег", "Остап", "Окслав",
                    "Петр", "Павел", "Прохор", "Панкрат", "Пантелей",
                    "Роман", "Ринат",
                    "Семён",
                    "Тимофей", "Трофим", "Трифан",
                    "Федот", "Филипп", "Фёдор",
                    "Харитон",
                    "Юлий", "Юрий",
                    "Яков", "Ян"};

            return names;
        }

        static String[] getArrayNameFemale() {

            String[] names = {"Анна", "Анастася", "Александра", "Афронья", "Алина", "Ангелина",
                    "Богиня", "Богдана",
                    "Валерия", "Виктория", "Василиса", "Владислава",
                    "Галина", "Грация", "Глаша",
                    "Дарья", "Дуня",
                    "Евгения", "Елена", "Елизавета", "Екатерина",
                    "Жана", "Жаклин",
                    "Зоя",
                    "Ирина", "Ирма", "Иллаиза", "Инга",
                    "Катерина", "Клавдия", "Колина", "Ксения",
                    "Леся",
                    "Марина", "Мона", "Мила",
                    "Нина", "Надежда",
                    "Ольга", "Осина", "Оксана",
                    "Патриция", "Павлина", "Песня",
                    "Рима",
                    "Селена", "София", "Соня", "Стела", "Сяблята",
                    "Татьяна", "Талия", "Трифанна",
                    "Фекла", "Фрида",
                    "Юлия", "Юнона",
                    "Яна"};

            return names;
        }

        static String[] getArrayFam(boolean isMale) {

            String[] fams = {"Аверкиев", "Авдеев", "Акименко", "Антонов", "Акимов", "Андреев", "Арапов", "Акимов",
                    "Бабушкин", "Баканов", "Белов", "Бедин", "Бродов", "Бодров", "Бесов", "Блинов", "Бояров",
                    "Ванин", "Вашкин", "Васильев", "Вешков", "Вишневский", "Войнов", "Волков", "Высоцкий",
                    "Гашников", "Гаврилов", "Ганин", "Герцин", "Гирин", "Грибов", "Горшвок", "Грядкин", "Грачёв",
                    "Данилин", "Даров", "Демин", "Дедушкин", "Думов", "Дубов", "Демидов", "Думский", "Дымов",
                    "Егоров", "Ерошин", "Енотов", "Елкин", "Епифанцев", "Ежов",
                    "Журавлёв", "Жуков", "Желтый", "Жарков", "Жилов",
                    "Заяцев", "Зотов", "Заходеров", "Знаков", "Зеленицкий", "Завьялов",
                    "Исаева", "Иванов", "Ильин", "Иноземцев", "Ирин",
                    "Кабаков", "Кузнецов", "Кротов", "Кинков", "Kострыкин", "Козлов", "Кузин",
                    "Лабанов", "Лесов", "Лапухов", "Ленин", "Летов", "Листьев",
                    "Малинин", "Мамин", "Минин", "Меншёв", "Молочный", "Мослов", "Муров",
                    "Назаров", "Новиков", "Носов", "Никифоров", "Никитин", "Негодин",
                    "Орденов", "Образцов", "Орлов", "Обрядин", "Ослов",
                    "Патрушев", "Петров", "Поликарпов", "Понкратов", "Пожарский", "Пронин", "Петухов",
                    "Ранин", "Реутов", "Рознов", "Рябчиков", "Решаев",
                    "Салов", "Селянин", "Скворцов", "Свиридов", "Стрельцов", "Сударев", "Синий", "Смольный",
                    "Табаков", "Титов", "Тиховнов", "Таров", "Темник", "Tюленев", "Тихорецкий", "Топоров",
                    "Усов", "Уваров", "Укесов", "Удочкин", "Упрямов",
                    "Хабаров", "Ханин", "Хитров", "Хворов", "Ходоров", "Хрипов", "Хорошев", "Худов",
                    "Цепин", "Цоболов", "Цурев", "Ципкин", "Цуканов",
                    "Шарунов", "Шестов", "Шаповалов", "Щеголяев", "Шершнев", "Шукин",
                    "Щебетов", "Щебатков", "Щекин", "Щипков",
                    "Эронов",
                    "Яковлев", "Якин", "Яшин", "Ямов", "Ярулин"
            };

            if (!isMale) {
                String[] result = new String[fams.length];
                for (int i = 0; i < fams.length; i++) {
                    String fam = fams[i];
                    if (fam.endsWith("ый")) {
                        fams[i] = fam.replace("ый", "ая");
                    } else if (fam.endsWith("ий")) {
                        fams[i] = fam.replace("ый", "ая");
                    } else {
                        fams[i] = fam.concat("a");
                    }
                }
                //return result;
            }
            return fams;
        }

        static String[] getArraySurnames(boolean isMale) {

            String[] maleSurnames = getArrayNameMale();
            String[] result = new String[maleSurnames.length];

            for (int i = 0; i < maleSurnames.length; i++) {
                String name = maleSurnames[i];
                if (name.endsWith("ий")) {
                    result[i] = name.replace("ий", isMale ? "ьевич" : "ьевна");
                } else if (name.endsWith("й")) {
                    result[i] = name.replace("й", isMale ? "евич" : "евна");
                } else {
                    result[i] = name.concat(isMale ? "ович" : "овна");
                }
            }

            return result;
        }

        static String randomName(int type, boolean isMale) {

            String[] names = switch (type) {
                case 1 -> getArrayFam(isMale);
                case 2 -> (isMale ? getArrayNameMale() : getArrayNameFemale());
                case 3 -> getArraySurnames(isMale);
                default -> new String[]{""};

            };

            return names[random.nextInt(names.length - 1)];
        }

        public static Date randomDate() {
            int yearRnd = random.nextInt(1930, 2008);
            int monthRnd = random.nextInt(1, 13);
            int dayRnd = random.nextInt(1, YearMonth.of(yearRnd, monthRnd).lengthOfMonth() + 1);

            return Date.valueOf(LocalDate.of(yearRnd, monthRnd, dayRnd));
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
                    "Центарльяная", "Крестьянская", "Загородная", "Зеленая", "Петра первого",
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
            if (random.nextDouble() < 0.8) {
                int number = random.nextInt(99) + 1;
                namePart += number;
            }

            return String.format("%s@%s", namePart, domain);
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

        public static Client randomClient() {

            boolean isMale = random.nextBoolean();
            String fam = randomName(1, isMale);
            String im = randomName(2, isMale);
            Date dateOfBirth = randomDate();
            String phoneNumber = randomPhoneNumber();
            String Email = randomEmail(fam, im);
            String Address = randomAddress();

            Client client = new Client();
            client.setFirstName(fam);
            client.setLastName(im);
            client.setBirthDate(dateOfBirth);
            client.setPhoneNumber(phoneNumber);
            client.setEmailAddress(Email);
            client.setAddress(Address);
            client.setComment("");

            return client;

        }
    }
}
