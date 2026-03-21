package ru.storage.enums;

public enum TypeMovements {
    DEBIT("Приход"),
    CREDIT("Расход");

    private final String title;

    TypeMovements(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
