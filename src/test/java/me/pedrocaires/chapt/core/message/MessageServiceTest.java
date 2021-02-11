package me.pedrocaires.chapt.core.message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    MessageDao messageDao;

    MessageService messageService;

    @BeforeEach
    void init() {
        this.messageService = new MessageService(messageDao);
    }

    @Test
    void shouldNotInsertWhenMessageAddressesNoUser() throws SQLException {
        var message = new Message();
        var from = 1;

        messageService.insert(from, message);

        verify(messageDao, never()).insert(from, message);
    }

    @Test
    void shouldInsertMessage() throws SQLException {
        var message = new Message();
        message.setTo(1);
        var from = 1;

        messageService.insert(from, message);

        verify(messageDao).insert(from, message);
    }

    @Test
    void shouldParseLastMessages() throws SQLException {
        var from = 1;
        var to = 2;
        var receivedMessage = "message received";
        var sentMessage = "message sent";
        var lastMessageRequest = new LastMessageRequest();
        lastMessageRequest.setFrom(from);
        var messages = Arrays.asList(new Message(to, sentMessage),
                new Message(from, receivedMessage));
        when(messageDao.selectLastMessages(lastMessageRequest)).thenReturn(messages);

        var lastMessages = messageService.selectLastMessages(lastMessageRequest);

        assertTrue(lastMessages.get(0).isSent());
        assertFalse(lastMessages.get(1).isSent());
        assertEquals(sentMessage, lastMessages.get(0).getContent());
        assertEquals(receivedMessage, lastMessages.get(1).getContent());
    }
}
