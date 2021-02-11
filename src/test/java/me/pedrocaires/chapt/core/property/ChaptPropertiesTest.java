package me.pedrocaires.chapt.core.property;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChaptPropertiesTest {

    String validStringKey = "validStringKey";
    String validStringValue = "validString";
    String invalidStringKey = "invalidStringKey";
    String validIntKey = "validIntKey";
    String invalidIntKey = "invalidIntKey";
    int validIntValue = 1;
    String envVarKey = "envVar";
    String notEnvVar1Key = "notEnvVar1";
    String notEnvVar2Key = "notEnvVar2";
    String notEnvVar1 = "${PATH";
    String notEnvVar2 = "PATH}";

    @Test
    void shouldLoadString() {
        var stringValue = ChaptProperties.getString(validStringKey);

        assertEquals(validStringValue, stringValue);
    }

    @Test
    void shouldBeEmptyStringWhenKeyDoesNotExists() {
        var stringValue = ChaptProperties.getString(invalidStringKey);

        assertEquals("", stringValue);
    }

    @Test
    void shouldLoadInt() {
        var intValue = ChaptProperties.getInt(validIntKey);

        assertEquals(validIntValue, intValue);
    }

    @Test
    void shouldBe0WhenKeyDoesNotExists() {
        var intValue = ChaptProperties.getInt(invalidIntKey);

        assertEquals(0, intValue);
    }

    @Test
    void shouldBe0WhenKeyIsString() {
        var intValue = ChaptProperties.getInt(validStringKey);

        assertEquals(0, intValue);
    }

    @Test
    void shouldReadEnvironmentVariables() {
        var envVar = ChaptProperties.getString(envVarKey);

        assertEquals(System.getenv("PATH"), envVar);
    }

    @Test
    void shouldNotReadNonEnvironmentVariableSyntax() {
        var stringValue1 = ChaptProperties.getString(notEnvVar1Key);
        var stringValue2 = ChaptProperties.getString(notEnvVar2Key);

        assertEquals(notEnvVar1, stringValue1);
        assertEquals(notEnvVar2, stringValue2);
    }


}
