package dev.auth.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import dev.ApplicationContextListener;

@Repository
public class LoginDao {

    public boolean login(ServletContext ctx, String username, String password) {

        DataSource ds = ApplicationContextListener.getReadDataSource(ctx);

        String sql = "SELECT user_id FROM users WHERE user_id=? AND password=?";

        try (
            Connection con = ds.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)
        ) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}