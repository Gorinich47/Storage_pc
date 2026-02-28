package ru.storage.model;

public enum PostEmployee {
    MANAGER("Управляющий"),
    WORKER("Сотрудник"),
    CLEANER("Уборщик"),
    FIXER("Мастер");

    private final String title;

    PostEmployee(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
