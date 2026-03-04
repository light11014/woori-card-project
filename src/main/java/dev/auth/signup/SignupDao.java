package dev.auth.signup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class SignupDao {

	private final DataSource dataSource;

	// 생성자 주입
	public SignupDao(@Qualifier("writeDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

	/**
     * 중복 아이디 체크
     * @return true = 이미 존재함, false = 사용 가능
     */
	public boolean existsUser(String username) throws SQLException {
		String sql = "SELECT user_id FROM users WHERE user_id = ?";

		try (Connection con = dataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(sql)) {

			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		}
	}
	
	/**
     * 신규 유저 INSERT
     * @return 삽입된 행 수 (성공 시 1)
     */
    public int insertUser(String username, String password) throws SQLException {
        String sql = "INSERT INTO users(user_id, password) VALUES (?, ?)";

        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            return stmt.executeUpdate();
        }
    }
}
