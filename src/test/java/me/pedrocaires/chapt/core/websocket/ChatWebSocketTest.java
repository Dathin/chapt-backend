package me.pedrocaires.chapt.core.websocket;

import me.pedrocaires.chapt.core.exception.InvalidJwtException;
import me.pedrocaires.chapt.core.jwt.JwtService;
import me.pedrocaires.chapt.core.message.Message;
import me.pedrocaires.chapt.core.message.MessageService;
import me.pedrocaires.chapt.core.testinterface.NoParameterConstructorTest;
import me.pedrocaires.chapt.core.user.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static me.pedrocaires.chapt.core.constants.GeneralConstant.PRINCIPAL;
import static me.pedrocaires.chapt.core.testconfig.Assertions.assertClose;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatWebSocketTest implements NoParameterConstructorTest {

    @Mock
    MessageService messageService;

    @Mock
    Session session;

    @Mock
    Map<String, Object> userProperties;

    @Mock
    RemoteEndpoint.Basic basic;

    ChatWebSocket chatWebSocket;

    @BeforeEach
    void init() throws NoSuchFieldException, IllegalAccessException {
        chatWebSocket = new ChatWebSocket(messageService);
        resetUsersMap();
    }

    private void resetUsersMap() throws NoSuchFieldException, IllegalAccessException {
        var field = ChatWebSocket.class.getDeclaredField("USERS");
        field.setAccessible(true);
        var modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, new ConcurrentHashMap<>());
    }

    @Test
    void shouldPutUserPrincipalOnOpen() {
        when(session.getUserProperties()).thenReturn(userProperties);

        chatWebSocket.onOpen(session);

        verify(userProperties).put(eq(PRINCIPAL), any(UserPrincipal.class));
    }

    @Test
    void shouldAuthenticate() throws EncodeException, IOException, SQLException {
        var userId = 1;
        var to = 2;
        var validToken = JwtService.issueToken(userId);
        mockUserPrincipal(new UserPrincipal());

        chatWebSocket.onMessage(session, new Message(to, validToken));

        assertEquals(1, ChatWebSocket.USERS.size());
        assertEquals(session, ChatWebSocket.USERS.get(userId));
    }

    //TODO: logger
    @Test
    void shouldNotAuthenticateWhenInvalidToken() {
        var to = 2;
        var invalidToken = "Bearer eyJhbGciOiJIUzM4NCJ9.eyJleHAiOjE2MDY3OTc3Nzl9.Q5sKWROwbq-sz4vE9y4aHF2_Owkvj0vXMvjRh50Iw88_dDmdCGcSgzHgJTuogVUW";
        var message = new Message(to, invalidToken);
        mockUserPrincipal(new UserPrincipal());

        assertThrows(InvalidJwtException.class, () -> chatWebSocket.onMessage(session, message));
    }

    @Test
    void shouldSkipAuthenticationIfAlreadySignedIn() throws EncodeException, IOException, SQLException {
        var userPrincipal = new UserPrincipal();
        userPrincipal.setAuthenticated(true);
        mockUserPrincipal(userPrincipal);

        chatWebSocket.onMessage(session, new Message());

        assertEquals(0, ChatWebSocket.USERS.size());

    }

    @Test
    void shouldPersistMessage() throws EncodeException, IOException, SQLException {
        var message = new Message();
        var userId = 1;
        var userPrincipal = new UserPrincipal();
        userPrincipal.setUserId(userId);
        userPrincipal.setAuthenticated(true);
        mockUserPrincipal(userPrincipal);

        chatWebSocket.onMessage(session, message);

        verify(messageService).insert(userId, message);
    }


    @Test
    void shouldSendMessageToReceiverIfOn() throws EncodeException, IOException, SQLException {
        var to = 2;
        var message = new Message();
        message.setTo(to);
        var userPrincipal = new UserPrincipal();
        userPrincipal.setAuthenticated(true);
        mockUserPrincipal(userPrincipal);
        when(session.getBasicRemote()).thenReturn(basic);
        ChatWebSocket.USERS.put(to, session);

        chatWebSocket.onMessage(session, message);

        verify(basic).sendObject(message);
    }

    @Test
    void shouldNotSendMessageToReceiverIfOff() throws EncodeException, IOException, SQLException {
        var message = new Message();
        message.setTo(2);
        var userPrincipal = new UserPrincipal();
        userPrincipal.setAuthenticated(true);
        mockUserPrincipal(userPrincipal);

        chatWebSocket.onMessage(session, message);

        verify(basic, never()).sendObject(message);
    }

    @Test
    void shouldRemoveUserFromMap() throws Exception {
        var userId = 1;
        var userPrincipal = new UserPrincipal();
        userPrincipal.setUserId(userId);
        mockUserPrincipal(userPrincipal);
        ChatWebSocket.USERS.put(userId, session);

        chatWebSocket.onClose(session);

        assertNull(ChatWebSocket.USERS.get(userId));
        assertClose(session);
    }

    @Test
    void shouldClose() throws Throwable {
        var th = new Throwable();

        chatWebSocket.onError(session, th);

        assertClose(session);
    }

    @Test
    @Override
    public void shouldInstantiateClassWithNoParameters() {
        new ChatWebSocket();
    }

    private void mockUserPrincipal(UserPrincipal userPrincipal) {
        when(session.getUserProperties()).thenReturn(userProperties);
        when(userProperties.get(PRINCIPAL)).thenReturn(userPrincipal);
    }
}
