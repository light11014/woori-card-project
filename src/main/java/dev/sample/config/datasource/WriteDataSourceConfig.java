package dev.sample.config.datasource;

/**
 * WRITE 전용 DataSource 설정
 * - 트랜잭션 대상
 * - Master DB
 */
public final class WriteDataSourceConfig {
    private WriteDataSourceConfig() {}

    public static final String JDBC_URL =
        "jdbc:mysql://localhost:3306/card_db?serverTimezone=Asia/Seoul";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "1234";

    // 풀 튜닝
    // ReadDataSourceConfig와 동일
    public static final String POOL_NAME = "WRITE_POOL";
    public static final int MAX_POOL_SIZE = 10;
    public static final int MIN_IDLE = 2;
    public static final long CONNECTION_TIMEOUT = 3000;
    public static final long IDLE_TIMEOUT = 600000;
    public static final long MAX_LIFETIME = 1800000;
}