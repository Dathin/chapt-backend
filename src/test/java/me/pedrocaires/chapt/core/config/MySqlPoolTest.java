package me.pedrocaires.chapt.core.config;

import me.pedrocaires.chapt.core.testconfig.MysqlPoolResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MysqlPoolResolver.class)
class MySqlPoolTest {

    Connection connection;

    @BeforeEach
    void init(Connection connection) {
        this.connection = connection;
    }

    @Test
    void shouldMockMysqlConnection() throws SQLException {
        assertEquals(connection, MySqlPool.getConnection());
    }

}
