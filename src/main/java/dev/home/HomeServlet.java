package dev.home;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import dev.trend.QuarterlyTrend;
import dev.trend.QuarterlyTrendDao;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
	
	private QuarterlyTrendDao dao;
	
	@Override
    public void init() throws ServletException {
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        dao = ctx.getBean(QuarterlyTrendDao.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<QuarterlyTrend> rows = dao.findRecentTwoQuarters();
        req.setAttribute("rows", rows);
        
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/home.jsp");
        rd.forward(req, resp);
    }
}