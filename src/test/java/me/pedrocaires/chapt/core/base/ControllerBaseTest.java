package me.pedrocaires.chapt.core.base;

import me.pedrocaires.chapt.core.exception.ChaptException;
import me.pedrocaires.chapt.core.exception.MethodNotAllowed;
import me.pedrocaires.chapt.core.exception.UnexpectedException;
import me.pedrocaires.chapt.core.json.JsonService;
import me.pedrocaires.chapt.core.message.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static me.pedrocaires.chapt.core.constants.GeneralConstant.USER_ID;
import static me.pedrocaires.chapt.core.testconfig.Assertions.assertServletStatusCode;
import static me.pedrocaires.chapt.core.testconfig.Assertions.assertServletWroteObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ControllerBaseTest {

    @Mock
    HttpServletRequest req;

    @Mock
    HttpServletResponse resp;

    @Mock
    PrintWriter printWriter;

    @Test
    void shouldThrowMethodNotAllowedDoCustomGet() {
        var controllerBase = new ControllerBase() {
        };

        assertThrows(MethodNotAllowed.class, () -> controllerBase.doCustomGet(req, resp));
    }

    @Test
    void shouldThrowMethodNotAllowedDoCustomPost() {
        var controllerBase = new ControllerBase() {
        };

        assertThrows(MethodNotAllowed.class, () -> controllerBase.doCustomPost(req, resp));
    }

    @Test
    void shouldGetUserId() {
        var controllerBase = new ControllerBase() {
        };
        var userId = 1;
        when(req.getAttribute(USER_ID)).thenReturn(1);

        var userIdFromController = controllerBase.getUserId(req);

        assertEquals(userId, userIdFromController);
    }

    @Test
    void shouldWriteGet() throws IOException {
        var message = new Message(1, "messageTest");
        var controllerBase = new ControllerBase() {
            @Override
            @SuppressWarnings("unchecked")
            public Message doCustomGet(HttpServletRequest req, HttpServletResponse resp) {
                return message;
            }
        };
        when(resp.getWriter()).thenReturn(printWriter);

        controllerBase.doGet(req, resp);

        assertServletStatusCode(resp, 200);
        assertServletWroteObject(printWriter, JsonService.writeJsonObject(message));
    }

    @Test
    void shouldWritePost() throws IOException {
        var message = new Message(1, "messageTest");
        var controllerBase = new ControllerBase() {
            @Override
            @SuppressWarnings("unchecked")
            public Message doCustomPost(HttpServletRequest req, HttpServletResponse resp) {
                return message;
            }
        };
        when(resp.getWriter()).thenReturn(printWriter);

        controllerBase.doPost(req, resp);

        assertServletStatusCode(resp, 200);
        assertServletWroteObject(printWriter, JsonService.writeJsonObject(message));
    }

    @Test
    void shouldCatchChaptException() throws IOException {
        var statusCode = 400;
        var chaptException = new ChaptException("messageChapt", statusCode) {
        };
        var controllerBase = new ControllerBase() {
            @Override
            public <T> T doCustomGet(HttpServletRequest req, HttpServletResponse resp) {
                throw chaptException;
            }
        };
        when(resp.getWriter()).thenReturn(printWriter);

        controllerBase.doGet(req, resp);

        assertServletStatusCode(resp, statusCode);
        assertServletWroteObject(printWriter, JsonService.writeJsonObject(chaptException.toErrorDto()));
    }

    @Test
    void shouldCatchUnexpectedException() throws IOException {
        var statusCode = 500;
        var unexpectedException = new RuntimeException();
        var controllerBase = new ControllerBase() {
            @Override
            public <T> T doCustomGet(HttpServletRequest req, HttpServletResponse resp) {
                throw unexpectedException;
            }
        };
        when(resp.getWriter()).thenReturn(printWriter);

        controllerBase.doGet(req, resp);

        assertServletStatusCode(resp, statusCode);
        assertServletWroteObject(printWriter, JsonService.writeJsonObject(new UnexpectedException().toErrorDto()));
    }
}
