package me.pedrocaires.chapt.core.enumerator;

import me.pedrocaires.chapt.core.constants.GeneralConstant;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum HttpHeader {
    AUTHORIZATION("Authorization", true),
    CONTENT_TYPE("Content-type", true),
    ALLOW_ORIGIN("Access-Control-Allow-Origin", false),
    ALLOW_METHODS("Access-Control-Allow-Methods", false),
    ALLOW_HEADERS("Access-Control-Allow-Headers", false);

    private final String value;
    private final boolean allowedHeader;

    HttpHeader(String value, boolean allowedHeader) {
        this.value = value;
        this.allowedHeader = allowedHeader;
    }

    public static String getAllowedHeaders() {
        return Arrays.stream(values())
                .filter(httpHeader -> httpHeader.allowedHeader)
                .map(HttpHeader::getValue)
                .collect(Collectors.joining(GeneralConstant.COMMA_DELIMITER));
    }

    public String getValue() {
        return value;
    }
}
