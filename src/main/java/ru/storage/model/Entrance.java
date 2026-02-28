package ru.storage.model;

public enum Entrance {
    DOOR("Дверь"),
    GATE("Ворота"),
    DOOR_IN_GATE("Дверь в воротах");

    private final String title;

    Entrance(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
