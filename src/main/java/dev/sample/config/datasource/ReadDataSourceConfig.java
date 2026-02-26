package dev.sample.config.datasource;

/**
 * READ 전용 DataSource 설정
 * - 조회 전용
 * - Replica DB
 */
public final class ReadDataSourceConfig {
    private ReadDataSourceConfig() {}

    public static final String JDBC_URL =
        "jdbc:mysql://localhost:3306/card_db?serverTimezone=Asia/Seoul";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "1234";

    // 조회 트래픽 대비
    public static final String POOL_NAME = "READ_POOL";
    public static final int MAX_POOL_SIZE = 20; // 최대 커넥션 수 20
    public static final int MIN_IDLE = 4; // 항상 대기 상태로 유지할 커넥션 수
    public static final long CONNECTION_TIMEOUT = 3000; // 커넥션 waiting 시간
    public static final long IDLE_TIMEOUT = 600000; // 커넥션 풀 제거 시간
    public static final long MAX_LIFETIME = 1800000; // 커넥션 수명
}