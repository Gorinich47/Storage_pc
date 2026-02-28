package ru.storage.model;

public enum StatusBox {
    FREE("Свободен"),
    BUSY("Занято"),
    FIXED("В ремонте"),
    CLOSED("Закрыто");

    private final String title;

    StatusBox(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
