package me.pedrocaires.chapt.core.property;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PropertiesTest {

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
        var stringValue = Properties.getPropertyString(validStringKey);

        assertEquals(validStringValue, stringValue);
    }

    @Test
    void shouldBeEmptyStringWhenKeyDoesNotExists() {
        var stringValue = Properties.getPropertyString(invalidStringKey);

        assertEquals("", stringValue);
    }

    @Test
    void shouldLoadInt() {
        var intValue = Properties.getPropertyInt(validIntKey);

        assertEquals(validIntValue, intValue);
    }

    @Test
    void shouldBe0WhenKeyDoesNotExists() {
        var intValue = Properties.getPropertyInt(invalidIntKey);

        assertEquals(0, intValue);
    }

    @Test
    void shouldBe0WhenKeyIsString() {
        var intValue = Properties.getPropertyInt(validStringKey);

        assertEquals(0, intValue);
    }

    @Test
    void shouldReadEnvironmentVariables() {
        var envVar = Properties.getPropertyString(envVarKey);

        assertEquals(System.getenv("PATH"), envVar);
    }

    @Test
    void shouldNotReadNonEnvironmentVariableSyntax() {
        var stringValue1 = Properties.getPropertyString(notEnvVar1Key);
        var stringValue2 = Properties.getPropertyString(notEnvVar2Key);

        assertEquals(notEnvVar1, stringValue1);
        assertEquals(notEnvVar2, stringValue2);
    }


}
