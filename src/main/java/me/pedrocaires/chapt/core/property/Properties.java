package me.pedrocaires.chapt.core.property;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

import static me.pedrocaires.chapt.core.constants.GeneralConstant.EMPTY_STRING;

public class Properties {

    private static final Map<String, String> PROPERTIES;

    static {
        var properties = new ArrayList<Property>();
        var propertiesReader = propertiesReader();
        listReadProperties(properties, propertiesReader);
        PROPERTIES = Collections.unmodifiableMap(mapListedProperties(properties));
    }

    private Properties() {

    }

    public static String getPropertyString(String key) {
        var property = PROPERTIES.get(key);
        return property == null ? EMPTY_STRING : property;
    }

    public static int getPropertyInt(String key) {
        try {
            return Integer.parseInt(PROPERTIES.get(key));
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private static Map<String, String> mapListedProperties(ArrayList<Property> properties) {
        var propertiesMap = new HashMap<String, String>();
        properties.forEach(property -> propertiesMap.put(property.getKey(), property.getValue()));
        return propertiesMap;
    }

    private static void listReadProperties(ArrayList<Property> properties, BufferedReader propertiesReader) {
        propertiesReader.lines().forEach(line -> {
            var equalsSignPosition = line.indexOf('=');
            properties.add(new Property(getKey(equalsSignPosition, line), getValue(equalsSignPosition, line)));
        });
    }

    private static BufferedReader propertiesReader() {
        var inputStream = Properties.class.getClassLoader().getResourceAsStream("props.txt");
        return new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)));
    }

    private static String getKey(int position, String line) {
        return line.substring(0, position);
    }

    private static String getValue(int position, String line) {
        var value = line.substring(position + 1);
        return isEnvironmentVariableSyntax(value) ? getEnvironmentVariable(value) : value;
    }

    private static boolean isEnvironmentVariableSyntax(String value) {
        return value.startsWith("${") && value.trim().endsWith("}");
    }

    private static String getEnvironmentVariable(String value) {
        return System.getenv(value.substring(2, value.length() - 1));
    }

}
