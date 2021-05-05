package me.pedrocaires.chapt.core.contact;

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
class ContactDaoTest {

    @Mock
    PreparedStatement preparedStatement;

    @Mock
    ResultSet rs;

    Connection connection;

    ContactDao contactDao;

    @BeforeEach
    void init(Connection connection) {
        this.connection = connection;
        this.contactDao = new ContactDao();
    }

    @AfterEach
    void dispose() throws Exception {
        assertClose(connection);
    }

    @Test
    void shouldReturnContactsByUserId() throws SQLException {
        var userId = 1;
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);

        var contactsByUser = contactDao.getContactsByUserId(userId);

        assertEquals(1, contactsByUser.size());
    }

    @Test
    void shouldInsertContact() throws SQLException {
        var contact = new Contact(1, 2);
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);

        contactDao.insert(contact);

        verify(preparedStatement).execute();
    }
}
