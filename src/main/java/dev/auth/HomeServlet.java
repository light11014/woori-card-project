package dev.auth;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import dev.ApplicationContextListener;
import dev.trend.JdbcQuarterlyTrendDao;
import dev.trend.QuarterlyTrend;
import dev.trend.QuarterlyTrendDao;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        ServletContext ctx = getServletContext();
        DataSource ds = ApplicationContextListener.getReadDataSource(ctx);

        QuarterlyTrendDao dao = new JdbcQuarterlyTrendDao(ds);
        List<QuarterlyTrend> rows = dao.findAllQuarterlyTrends();

        req.setAttribute("rows", rows);

        RequestDispatcher rd =
                req.getRequestDispatcher("/WEB-INF/views/home.jsp");
        rd.forward(req, resp);
    }
}