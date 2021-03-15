package me.pedrocaires.chapt.core.testconfig.resolver;

import me.pedrocaires.chapt.core.config.MySqlPool;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MysqlPoolResolver implements ParameterResolver {

    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(MysqlPoolResolver.class);
    private final DataSource dataSource = mock(DataSource.class);
    private Connection connection;

    @Override
    public boolean supportsParameter(
            ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Connection.class;
    }

    @Override
    public Object resolveParameter(
            ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        try {
            mockDataSource();
            mockDataSourceGetConnection();
        } catch (NoSuchFieldException | IllegalAccessException | SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("There was an error creating " + getClass().getSimpleName());
        }
        return extensionContext
                .getStore(NAMESPACE)
                .getOrComputeIfAbsent(
                        parameterContext, key -> connection, Connection.class);
    }

    private void mockDataSourceGetConnection() throws SQLException {
        connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
    }

    private void mockDataSource() throws NoSuchFieldException, IllegalAccessException {
        var field = MySqlPool.class.getDeclaredField("DATA_SOURCE");
        field.setAccessible(true);
        var modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, dataSource);
    }

}

