package dev.auth.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private LoginService loginService;

    @Override
    public void init() throws ServletException {

        ApplicationContext ctx =
                WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

        loginService = ctx.getBean(LoginService.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        boolean result = loginService.login(getServletContext(), username, password);

        if (result) {

            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) oldSession.invalidate();

            HttpSession session = request.getSession(true);
            session.setAttribute("loginUser", username);
            session.setMaxInactiveInterval(30 * 60);

            response.sendRedirect(request.getContextPath() + "/home");

        } else {

            response.sendRedirect("login.html?error=true");

        }
    }
}