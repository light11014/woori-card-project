package dev.auth.login;

import javax.servlet.ServletContext;

public class LoginService {

    private LoginDao loginDao = new LoginDao();

    public boolean login(ServletContext ctx, String username, String password) {

        if (username == null || password == null) {
            return false;
        }

        return loginDao.login(ctx, username, password);
    }
}