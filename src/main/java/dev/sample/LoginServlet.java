package dev.sample;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	private static final String VALID_USERNAME = "admin";
	private static final String VALID_PASSWORD = "1234";

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		ServletContext ctx = getServletContext();
		DataSource ds = ApplicationContextListener.getReadDataSource(ctx);

		if (isValidUser(ds, username, password)) {
			// 세션 고정 공격 방지
			HttpSession oldSession = request.getSession(false);
			if (oldSession != null)
				oldSession.invalidate();

			HttpSession session = request.getSession(true);
			session.setAttribute("loginUser", username);
			session.setMaxInactiveInterval(30 * 60);

			response.sendRedirect("trend/quarterly");

		} else {
			// 로그인 실패 → 로그인 페이지로 리다이렉트
			response.sendRedirect("login.html?error=true");
		}
	}

	private boolean isValidUser(DataSource ds, String username, String password) {
	    if (username == null || password == null) return false;

	    String sql = "SELECT user_id FROM users WHERE user_id=? AND password=?";

	    try (Connection con = ds.getConnection();
	         PreparedStatement pstmt = con.prepareStatement(sql)) {
	    	
	    	pstmt.setString(1, username);
	    	pstmt.setString(2, password);

	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	        	String userId = rs.getString("user_id");
	            return !userId.isEmpty();
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return false;
	}
}