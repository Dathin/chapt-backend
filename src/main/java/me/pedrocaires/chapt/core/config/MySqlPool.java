package me.pedrocaires.chapt.core.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import me.pedrocaires.chapt.core.property.ChaptProperties;

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
        hikariConfig.setDriverClassName(ChaptProperties.getString("mysqlDriverClassName"));
        hikariConfig.setJdbcUrl(ChaptProperties.getString("jdbcUrl"));
        hikariConfig.setUsername(ChaptProperties.getString("username"));
        hikariConfig.setPassword(ChaptProperties.getString("password"));

        hikariConfig.setMaximumPoolSize(ChaptProperties.getInt("maximumPoolSize"));
        hikariConfig.setConnectionTestQuery(ChaptProperties.getString("connectionTestQuery"));
        hikariConfig.setPoolName(ChaptProperties.getString("poolName"));

        hikariConfig.addDataSourceProperty("dataSource.cachePrepStmts", ChaptProperties.getString("cachePrepStmts"));
        hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSize", ChaptProperties.getString("prepStmtCacheSize"));
        hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", ChaptProperties.getString("prepStmtCacheSqlLimit"));
        hikariConfig.addDataSourceProperty("dataSource.useServerPrepStmts", ChaptProperties.getString("useServerPrepStmts"));

        try {
            return new HikariDataSource(hikariConfig);
        } catch (HikariPool.PoolInitializationException | IllegalArgumentException ex) {
            return null;
        }
    }

}
