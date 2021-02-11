package me.pedrocaires.chapt.core.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import me.pedrocaires.chapt.core.property.Properties;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MySqlPool {


    private static final DataSource DATA_SOURCE;

    static {
        DATA_SOURCE = dataSource();
    }

    private MySqlPool() {

    }

    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }

    private static DataSource dataSource() {

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(Properties.getPropertyString("mysqlDriverClassName"));
        hikariConfig.setJdbcUrl(Properties.getPropertyString("jdbcUrl"));
        hikariConfig.setUsername(Properties.getPropertyString("username"));
        hikariConfig.setPassword(Properties.getPropertyString("password"));

        hikariConfig.setMaximumPoolSize(Properties.getPropertyInt("maximumPoolSize"));
        hikariConfig.setConnectionTestQuery(Properties.getPropertyString("connectionTestQuery"));
        hikariConfig.setPoolName(Properties.getPropertyString("poolName"));

        hikariConfig.addDataSourceProperty("dataSource.cachePrepStmts", Properties.getPropertyString("cachePrepStmts"));
        hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSize", Properties.getPropertyString("prepStmtCacheSize"));
        hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", Properties.getPropertyString("prepStmtCacheSqlLimit"));
        hikariConfig.addDataSourceProperty("dataSource.useServerPrepStmts", Properties.getPropertyString("useServerPrepStmts"));

        try {
            return new HikariDataSource(hikariConfig);
        } catch (HikariPool.PoolInitializationException | IllegalArgumentException ex) {
            return null;
        }
    }

}
