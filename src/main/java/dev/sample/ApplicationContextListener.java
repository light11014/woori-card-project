package dev.sample;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;

import dev.sample.config.datasource.DataSourceKeys;
import dev.sample.config.datasource.HikariDataSourceFactory;

@WebListener
public class ApplicationContextListener implements ServletContextListener {

    // WRITE, READ DataSource
    private HikariDataSource writeDs;
    private HikariDataSource readDs;

    @Override
    public void contextInitialized(ServletContextEvent sce) { // application시작
        System.out.println("hi");

        ServletContext ctx = sce.getServletContext();

        // JDBC Driver 명시 로딩
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("MySQL Driver not found", e);
        }

        // Factory를 통해 DataSource 생성
        writeDs = HikariDataSourceFactory.createWriteDataSource();
        readDs  = HikariDataSourceFactory.createReadDataSource();

        // ServletContext에 등록 (키 통일)
        ctx.setAttribute(DataSourceKeys.WRITE_DS, writeDs);
        ctx.setAttribute(DataSourceKeys.READ_DS, readDs);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 애플리케이션 종료 시 커넥션 풀 자원 해제
        if (writeDs != null) writeDs.close();
        if (readDs != null) readDs.close();
    }
    
    public static DataSource getReadDataSource(ServletContext ctx) {
        return (DataSource) ctx.getAttribute(DataSourceKeys.READ_DS);
    }
    
    public static DataSource getWriteDataSource(ServletContext ctx) {
        return (DataSource) ctx.getAttribute(DataSourceKeys.WRITE_DS);
    }
}