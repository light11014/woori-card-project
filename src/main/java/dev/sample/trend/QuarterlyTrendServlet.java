package dev.sample.trend;

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

import dev.sample.ApplicationContextListener;

@WebServlet("/trend/quarterly")
public class QuarterlyTrendServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        ServletContext ctx = getServletContext();
        DataSource ds = ApplicationContextListener.getDataSource(ctx);

        QuarterlyTrendDao dao = new JdbcQuarterlyTrendDao(ds);
        List<QuarterlyTrend> rows = dao.findAllQuarterlyTrends();

        // 뷰에서 사용
        req.setAttribute("rows", rows);

        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/quarterlyTrend.jsp");
        rd.forward(req, resp);
    }
}