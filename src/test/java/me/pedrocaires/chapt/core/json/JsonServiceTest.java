package me.pedrocaires.chapt.core.json;

import me.pedrocaires.chapt.core.exception.ExceptionResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static me.pedrocaires.chapt.core.testconfig.assertion.Assertions.assertServletWroteObject;
import static me.pedrocaires.chapt.core.testconfig.mock.MockUtils.mockServletReader;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JsonServiceTest {

    @Mock
    HttpServletRequest req;

    @Mock
    HttpServletResponse resp;

    @Mock
    PrintWriter printWriter;

    @Test
    void shouldNotFailOnUnknownProperties() throws IOException {
        String jsonRepresentation = "{\"unknownProp\": \"any\"}";
        mockServletReader(req, jsonRepresentation);

        JsonService.readJsonObject(req, Object.class);
    }

    @Test
    void shouldWriteJsonToHttpServlet() throws IOException {
        int statusCode = 200;
        ExceptionResponse object = new ExceptionResponse();
        String jsonRepresentation = "{\"message\":null}";
        when(resp.getWriter()).thenReturn(printWriter);

        JsonService.writeJsonObjectToHttpServlet(object, statusCode, resp);

        verify(resp).setStatus(statusCode);
        assertServletWroteObject(printWriter, jsonRepresentation);
    }

}
