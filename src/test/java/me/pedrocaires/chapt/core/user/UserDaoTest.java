package me.pedrocaires.chapt.core.user;

import me.pedrocaires.chapt.core.testconfig.MysqlPoolResolver;
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

import static me.pedrocaires.chapt.core.testconfig.Assertions.assertClose;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, MysqlPoolResolver.class})
class UserDaoTest {

    @Mock
    PreparedStatement preparedStatement;

    @Mock
    ResultSet rs;

    Connection connection;

    UserDao userDao;

    @BeforeEach
    void init(Connection connection) throws SQLException {
        this.connection = connection;
        userDao = new UserDao();
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);
    }

    @AfterEach
    void dispose() throws Exception {
        assertClose(connection);
    }

    @Test
    void shouldInsertUser() throws SQLException {
        var user = new User();

        userDao.insert(user);
    }

    @Test
    void shouldGetUserLoginInfoByEMail() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);

        var user = userDao.getUserLoginInfoByEMail(new UserLoginRequest());

        assertNotNull(user);
    }

    @Test
    void shouldCloseStatementAndResultSetWhenConnectionIsClosed() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);

        var user = userDao.getUserLoginInfoByEMail(new UserLoginRequest());

        verify(preparedStatement).close();
        assertNotNull(user);
    }
}
