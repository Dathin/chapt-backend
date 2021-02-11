package me.pedrocaires.chapt.core.testconfig;

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

}
