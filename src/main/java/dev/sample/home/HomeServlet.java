package dev.sample.home;

import java.io.IOException;
import java.util.List;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;

import dev.sample.ApplicationContextListener;
import dev.sample.trend.*;

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