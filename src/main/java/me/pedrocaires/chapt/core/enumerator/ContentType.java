package me.pedrocaires.chapt.core.enumerator;

public enum ContentType {
    JSON("application/json");

    private final String value;

    ContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
