package dev.sample.config.datasource;

import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Hikari DataSource 생성 팩토리
 * - READ / WRITE 설정을 각각의 Config에서 가져와 조립
 */
public final class HikariDataSourceFactory {
    private HikariDataSourceFactory() {}

    public static HikariDataSource createWriteDataSource() {
        HikariConfig config = baseConfig();

        config.setJdbcUrl(WriteDataSourceConfig.JDBC_URL);
        config.setUsername(WriteDataSourceConfig.USERNAME);
        config.setPassword(WriteDataSourceConfig.PASSWORD);

        config.setPoolName(WriteDataSourceConfig.POOL_NAME);
        config.setMaximumPoolSize(WriteDataSourceConfig.MAX_POOL_SIZE);
        config.setMinimumIdle(WriteDataSourceConfig.MIN_IDLE);
        config.setConnectionTimeout(WriteDataSourceConfig.CONNECTION_TIMEOUT);
        config.setIdleTimeout(WriteDataSourceConfig.IDLE_TIMEOUT);
        config.setMaxLifetime(WriteDataSourceConfig.MAX_LIFETIME);

        return new HikariDataSource(config);
    }

    public static HikariDataSource createReadDataSource() {
        HikariConfig config = baseConfig();

        config.setJdbcUrl(ReadDataSourceConfig.JDBC_URL);
        config.setUsername(ReadDataSourceConfig.USERNAME);
        config.setPassword(ReadDataSourceConfig.PASSWORD);

        config.setPoolName(ReadDataSourceConfig.POOL_NAME);
        config.setMaximumPoolSize(ReadDataSourceConfig.MAX_POOL_SIZE);
        config.setMinimumIdle(ReadDataSourceConfig.MIN_IDLE);
        config.setConnectionTimeout(ReadDataSourceConfig.CONNECTION_TIMEOUT);
        config.setIdleTimeout(ReadDataSourceConfig.IDLE_TIMEOUT);
        config.setMaxLifetime(ReadDataSourceConfig.MAX_LIFETIME);

        return new HikariDataSource(config);
    }

    // READ, WRITE 공통 설정
    private static HikariConfig baseConfig() {
        HikariConfig config = new HikariConfig();

        Properties props = new Properties();
        props.setProperty("cachePrepStmts", "true");
        props.setProperty("prepStmtCacheSize", "250");
        props.setProperty("prepStmtCacheSqlLimit", "2048");
        props.setProperty("useServerPrepStmts", "true");

        config.setDataSourceProperties(props);
        return config;
    }
}