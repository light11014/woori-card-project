package dev.auth.login;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private LoginDao loginDao;

    public boolean login(ServletContext ctx, String username, String password) {

        if (username == null || password == null) {
            return false;
        }

        return loginDao.login(ctx, username, password);
    }
}