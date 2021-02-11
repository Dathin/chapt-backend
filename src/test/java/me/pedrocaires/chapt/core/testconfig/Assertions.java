package me.pedrocaires.chapt.core.testconfig;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import static org.mockito.Mockito.verify;

public class Assertions {

    private Assertions() {
    }

    public static void assertClose(AutoCloseable autoCloseable) throws Exception {
        verify(autoCloseable).close();
    }

    public static void assertServletStatusCode(HttpServletResponse resp, int statusCode) {
        verify(resp).setStatus(statusCode);
    }

    public static void assertServletWroteObject(PrintWriter printWriter, String object) {
        verify(printWriter).write(object);
        verify(printWriter).flush();
        verify(printWriter).close();
    }
}
