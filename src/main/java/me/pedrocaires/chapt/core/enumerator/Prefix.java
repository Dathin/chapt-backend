package me.pedrocaires.chapt.core.enumerator;

public enum Prefix {
    BEARER("Bearer ");

    private final String value;

    Prefix(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
