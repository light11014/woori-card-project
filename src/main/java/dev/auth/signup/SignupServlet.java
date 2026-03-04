package dev.auth.signup;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

    private SignupService signupService;

    @Override
    public void init() throws ServletException {
        // 스프링 컨테이너에서 SignupService Bean 꺼내기
        WebApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext(getServletContext());
        signupService = ctx.getBean(SignupService.class);
    }

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

        // 입력값 검증
        if (username == null || password == null
                || username.isEmpty() || password.isEmpty()) {
            response.sendRedirect("signup?error=empty");
            return;
        }

        // Service 호출 → 결과에 따라 redirect
        String result = signupService.signup(username, password);

        switch (result) {
            case "ok":
                response.sendRedirect("login.html");
                break;
            case "duplicate":
                response.sendRedirect("signup?error=duplicate");
                break;
            default: // "db"
                response.sendRedirect("signup?error=db");
                break;
        }
    }
}
