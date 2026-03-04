package dev.auth.signup;

import java.sql.SQLException;

import org.springframework.stereotype.Service;

@Service
public class SignupService {
	
	private final SignupDao signupDao;
	
	// 생성자 주입
    public SignupService(SignupDao signupDao) {
        this.signupDao = signupDao;
    }

    /**
     * 회원가입 처리
     * @return "ok"         - 성공
     *         "duplicate"  - 아이디 중복
     *         "db"         - DB 오류
     */
    public String signup(String username, String password) {

        try {
            // 중복 체크
            if (signupDao.existsUser(username)) {
                return "duplicate";
            }

            // INSERT
            signupDao.insertUser(username, password);
            return "ok";

        } catch (SQLException e) {
            e.printStackTrace();
            return "db";
        }
    }
}
