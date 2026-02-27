package dev.sample.config.datasource;

import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Hikari DataSource 생성 팩토리
 * - Docker/로컬 모두 동작하도록 환경변수 기반 설정 지원
 *
 * 우선순위:
 * 1) 환경변수(DB_WRITE_*, DB_READ_*)
 * 2) 기존 Config 상수(ReadDataSourceConfig / WriteDataSourceConfig)
 */
public final class HikariDataSourceFactory {
    private HikariDataSourceFactory() {}

    public static HikariDataSource createWriteDataSource() {
        HikariConfig config = baseConfig();

        // docker에서는 woori-mysql-source로, 로컬에서는 localhost로
        config.setJdbcUrl(buildJdbcUrl("WRITE", WriteDataSourceConfig.JDBC_URL));
        config.setUsername(getEnv("DB_WRITE_USER", WriteDataSourceConfig.USERNAME));
        config.setPassword(getEnv("DB_WRITE_PASSWORD", WriteDataSourceConfig.PASSWORD));

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

        // docker에서는 woori-mysql-replica로, 로컬에서는 localhost로
        config.setJdbcUrl(buildJdbcUrl("READ", ReadDataSourceConfig.JDBC_URL));
        config.setUsername(getEnv("DB_READ_USER", ReadDataSourceConfig.USERNAME));
        config.setPassword(getEnv("DB_READ_PASSWORD", ReadDataSourceConfig.PASSWORD));

        config.setPoolName(ReadDataSourceConfig.POOL_NAME);
        config.setMaximumPoolSize(ReadDataSourceConfig.MAX_POOL_SIZE);
        config.setMinimumIdle(ReadDataSourceConfig.MIN_IDLE);
        config.setConnectionTimeout(ReadDataSourceConfig.CONNECTION_TIMEOUT);
        config.setIdleTimeout(ReadDataSourceConfig.IDLE_TIMEOUT);
        config.setMaxLifetime(ReadDataSourceConfig.MAX_LIFETIME);

        return new HikariDataSource(config);
    }

    /**
     * mode: WRITE / READ
     * defaultJdbcUrl: 기존 상수 URL (로컬 기본값)
     */
    private static String buildJdbcUrl(String mode, String defaultJdbcUrl) {
        // 환경변수로 완전한 JDBC_URL을 주면 그걸 최우선 사용
        String full = System.getenv("DB_" + mode + "_JDBC_URL");
        if (full != null && !full.isBlank()) {
            return full.trim();
        }

        // 아니면 host/port/name으로 조립
        String host = System.getenv("DB_" + mode + "_HOST");
        String port = System.getenv("DB_" + mode + "_PORT");
        String name = System.getenv("DB_" + mode + "_NAME");

        boolean hasParts = (host != null && !host.isBlank())
                || (port != null && !port.isBlank())
                || (name != null && !name.isBlank());

        if (!hasParts) {
            // 아무 env도 없으면 기존 상수(= localhost) 사용
            return defaultJdbcUrl;
        }

        String h = (host == null || host.isBlank()) ? "localhost" : host.trim();
        String p = (port == null || port.isBlank()) ? "3306" : port.trim();
        String db = (name == null || name.isBlank()) ? "woori_card" : name.trim();

        // 기존 옵션 유지
        return "jdbc:mysql://" + h + ":" + p + "/" + db + "?serverTimezone=Asia/Seoul";
    }

    private static String getEnv(String key, String defaultValue) {
        String v = System.getenv(key);
        return (v == null || v.isBlank()) ? defaultValue : v.trim();
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