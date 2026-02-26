package dev.sample.config.datasource;

import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

// Hikari 설정/생성 공통 팩토리 만들기 (튜닝값 적용 지점)
public final class HikariDataSourceFactory { // final + private => 정적 메서드로만 사용하기 위함
    private HikariDataSourceFactory() {}

    public static HikariDataSource createWriteDataSource() { // 쓰기 전용 datasource 생성
        HikariConfig config = baseConfig();

        config.setJdbcUrl("jdbc:mysql://localhost:3306/woori_card?serverTimezone=Asia/Seoul");
        config.setUsername("root");
        config.setPassword("1234");

        // WRITE 튜닝
        config.setPoolName("WRITE_POOL");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(3000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        return new HikariDataSource(config);
    }

    public static HikariDataSource createReadDataSource() {
        HikariConfig config = baseConfig();

        config.setJdbcUrl("jdbc:mysql://localhost:3306/woori_card?serverTimezone=Asia/Seoul");
        config.setUsername("root");
        config.setPassword("1234");

        // READ 튜닝
        config.setPoolName("READ_POOL");
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(4);
        config.setConnectionTimeout(3000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        return new HikariDataSource(config);
    }

    private static HikariConfig baseConfig() {
        HikariConfig config = new HikariConfig();

        // MySQL 드라이버 성능 옵션
        Properties dsProps = new Properties();
        dsProps.setProperty("cachePrepStmts", "true"); // cache 활성화
        dsProps.setProperty("prepStmtCacheSize", "250"); // cache 개수 제한
        dsProps.setProperty("prepStmtCacheSqlLimit", "2048");
        dsProps.setProperty("useServerPrepStmts", "true");
        config.setDataSourceProperties(dsProps);

        return config;
    }
}