package dev.sample.home;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;

import dev.sample.ApplicationContextListener;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request,
	                     HttpServletResponse response)
	        throws ServletException, IOException {

	    request.getRequestDispatcher("/signup.html")
	           .forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
	                      HttpServletResponse response)
	        throws ServletException, IOException {

	    request.setCharacterEncoding("UTF-8");

	    String username = request.getParameter("username");
	    String password = request.getParameter("password");

	    if (username == null || password == null
	            || username.isEmpty() || password.isEmpty()) {

	        response.sendRedirect("signup?error=empty");
	        return;
	    }

	    ServletContext ctx = getServletContext();
	    DataSource ds = ApplicationContextListener.getReadDataSource(ctx);

	    try (Connection con = ds.getConnection()) {

	        // 1️⃣ 중복 체크
	        String checkSql = "SELECT user_id FROM users WHERE user_id=?";
	        try (PreparedStatement checkStmt = con.prepareStatement(checkSql)) {
	            checkStmt.setString(1, username);
	            ResultSet rs = checkStmt.executeQuery();

	            if (rs.next()) {
	                response.sendRedirect("signup?error=duplicate");
	                return;
	            }
	        }

	        // 2️⃣ INSERT
	        String insertSql = "INSERT INTO users(user_id, password) VALUES (?, ?)";
	        try (PreparedStatement insertStmt = con.prepareStatement(insertSql)) {
	            insertStmt.setString(1, username);
	            insertStmt.setString(2, password);
	            insertStmt.executeUpdate();
	        }

	        response.sendRedirect("login.html");

	    } catch (SQLException e) {
	        e.printStackTrace();
	        response.sendRedirect("signup?error=db");
	    }
	}
}