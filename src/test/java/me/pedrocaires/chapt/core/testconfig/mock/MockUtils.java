package me.pedrocaires.chapt.core.testconfig.mock;

import me.pedrocaires.chapt.core.json.JsonService;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.mockito.Mockito.when;

public class MockUtils {

    private MockUtils() {

    }

    public static void mockServletReader(HttpServletRequest req, String content) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new StringReader(content));
        when(req.getReader()).thenReturn(bufferedReader);
    }

    public static void mockServletReader(HttpServletRequest req, Object content) throws IOException {
        var jsonRepresentation = JsonService.writeJsonObject(content);
        mockServletReader(req, jsonRepresentation);
    }

    public static void mockServletAttribute(HttpServletRequest req, String attribute, Object value){
        when(req.getAttribute(attribute)).thenReturn(value);
    }

}
