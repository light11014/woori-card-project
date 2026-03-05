package dev;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import dev.db.HikariDataSourceFactory;

@Configuration
@ComponentScan(basePackages = "dev")
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        // 1. 드라이버 클래스를 명시적으로 로딩하여 SQLException 방지
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver를 찾을 수 없습니다.", e);
        }

        // 2. 팩토리를 호출하여 DataSource 생성
        return HikariDataSourceFactory.createReadDataSource(); 
    }
}