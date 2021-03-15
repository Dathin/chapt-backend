package me.pedrocaires.chapt.core.message;

import me.pedrocaires.chapt.core.testconfig.resolver.MysqlPoolResolver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static me.pedrocaires.chapt.core.testconfig.assertion.Assertions.assertClose;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, MysqlPoolResolver.class})
class MessageDaoTest {

    @Mock
    PreparedStatement preparedStatement;

    @Mock
    ResultSet rs;

    Connection connection;

    MessageDao messageDao;

    @BeforeEach
    void init(Connection connection) {
        this.connection = connection;
        this.messageDao = new MessageDao();
    }

    @AfterEach
    void dispose() throws Exception {
        assertClose(connection);
    }

    @Test
    void shouldInsertMessage() throws SQLException {
        var from = 1;
        var message = new Message();
        message.setTo(1);
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);

        messageDao.insert(from, message);

        verify(preparedStatement).execute();
    }

    @Test
    void shouldSelectLastMessages() throws SQLException {
        var from = 1;
        var to = 2;
        var pageSize = 2;
        var pageIndex = 3;
        var lastMessageRequest = new LastMessageRequest();
        lastMessageRequest.setFrom(from);
        lastMessageRequest.setTo(to);
        lastMessageRequest.setPageSize(pageSize);
        lastMessageRequest.setPageIndex(pageIndex);
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);

        var lastMessages = messageDao.selectLastMessages(lastMessageRequest);

        assertEquals(1, lastMessages.size());
        verify(preparedStatement).setInt(6, pageSize * pageIndex);
    }
}
