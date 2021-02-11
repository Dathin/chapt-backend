package me.pedrocaires.chapt.core.enumerator;

import me.pedrocaires.chapt.core.constants.GeneralConstant;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum HttpMethod {
    GET, POST, OPTIONS;//, PUT, DELETE

    public static String getCommaSeparatedValues() {
        return Arrays.stream(HttpMethod.values())
                .map(Enum::name)
                .collect(Collectors.joining(GeneralConstant.COMMA_DELIMITER));
    }


}
