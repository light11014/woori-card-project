package dev.sample;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

		if (isValidUser(username, password)) {
			// 세션 고정 공격 방지
			HttpSession oldSession = request.getSession(false);
			if (oldSession != null)
				oldSession.invalidate();

			HttpSession session = request.getSession(true);
			session.setAttribute("loginUser", username);
			session.setMaxInactiveInterval(30 * 60);

			response.sendRedirect("home");

		} else {
			// 로그인 실패 → 로그인 페이지로 리다이렉트
			response.sendRedirect("login.html?error=true");
		}
	}

	private boolean isValidUser(String username, String password) {
		// TODO: DB 조회로 변경
		if (username == null || password == null)
			return false;
		return VALID_USERNAME.equals(username.trim()) && VALID_PASSWORD.equals(password);
	}
}