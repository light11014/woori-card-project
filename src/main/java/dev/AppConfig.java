package dev;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import dev.db.HikariDataSourceFactory;

@Configuration
@ComponentScan("dev")
public class AppConfig {
	
	@Bean
    public DataSource writeDataSource() {
        return HikariDataSourceFactory.createWriteDataSource();
    }

    @Bean
    public DataSource readDataSource() {
        return HikariDataSourceFactory.createReadDataSource();
    }
}

