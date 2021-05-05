package me.pedrocaires.chapt.core.websocket;

import me.pedrocaires.chapt.core.jwt.JwtService;
import me.pedrocaires.chapt.core.message.Message;
import me.pedrocaires.chapt.core.message.MessageDao;
import me.pedrocaires.chapt.core.message.MessageService;
import me.pedrocaires.chapt.core.user.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static me.pedrocaires.chapt.core.constants.GeneralConstant.PRINCIPAL;

@ServerEndpoint(value = "/chat", decoders = ChatWebSocketMessageDecoder.class, encoders = ChatWebSocketMessageEncoder.class)
public class ChatWebSocket {

    public static final Logger LOGGER = LoggerFactory.getLogger(ChatWebSocket.class);

    protected static final Map<Integer, Session> USERS = new ConcurrentHashMap<>();
    private final MessageService messageService;

    public ChatWebSocket() {
        this.messageService = new MessageService(new MessageDao());
    }

    public ChatWebSocket(MessageService messageService) {
        this.messageService = messageService;
    }

    @OnOpen
    public void onOpen(Session session) {
        LOGGER.info("Opened new session");
        var userProperties = session.getUserProperties();
        userProperties.put(PRINCIPAL, new UserPrincipal());
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException, SQLException {
        LOGGER.info("Received message {}", message.getContent());
        handleAuthentication(session, message);
        messageService.insert(getUserPrincipal(session).getUserId(), message);
        broadcastMessage(message, getUserPrincipal(session).getUserId());
    }

    private void handleAuthentication(Session session, Message message) {
        var userPrincipal = getUserPrincipal(session);
        if (!userPrincipal.isAuthenticated()) {
            var userId = JwtService.validateToken(message.getContent());
            userPrincipal.setAuthenticated(true);
            userPrincipal.setUserId(userId);
            USERS.put(userId, session);
            LOGGER.info("Authenticated user {}", userPrincipal.getUserId());
        }
    }

    private void broadcastMessage(Message message, int userId) throws IOException, EncodeException {
        if (isToUserConnected(message.getTo())) {
            synchronized (USERS.get(message.getTo())) {
                USERS.get(message.getTo()).getBasicRemote().sendObject(message);
            }
            LOGGER.info("Sent message from {} to {}", userId, message.getTo());
        }
    }

    private boolean isToUserConnected(Integer to) {
        return to != null && USERS.get(to) != null;
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        var userPrincipalId = getUserPrincipal(session).getUserId();
        USERS.remove(userPrincipalId);
        session.close();
        LOGGER.info("User {} disconnected", userPrincipalId);
    }

    @OnError
    public void onError(Session session, Throwable th) throws IOException {
        var userPrincipalId = getUserPrincipal(session).getUserId();
        session.close();
        LOGGER.error("User {} had an error", userPrincipalId, th);
    }

    private UserPrincipal getUserPrincipal(Session session) {
        return (UserPrincipal) session.getUserProperties().get(PRINCIPAL);
    }
}
