package dev.sample.trend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

        // 1) 파라미터 (기본값 포함)
        int range = parseRange(req.getParameter("range"));      // 1/4/8
        String view = normalizeView(req.getParameter("view"));  // AMOUNT/RATIO

        // 2) 집계 테이블에서 조회
        ServletContext ctx = getServletContext();
        DataSource ds = ApplicationContextListener.getReadDataSource(ctx);

        QuarterlyTrendDao dao = new JdbcQuarterlyTrendDao(ds);
        List<QuarterlyTrend> all = dao.findAllQuarterlyTrends();

        // 3) 최근 N개 slice
        List<QuarterlyTrend> rows = sliceLast(all, range);

        // 4) 뷰 전달
        req.setAttribute("rows", rows);
        req.setAttribute("range", range);
        req.setAttribute("view", view);

        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/quarterlyTrend.jsp");
        rd.forward(req, resp);
    }

    private int parseRange(String s) {
        if (s == null) return 8;
        try {
            int n = Integer.parseInt(s);
            return (n == 1 || n == 4 || n == 8) ? n : 8;
        } catch (NumberFormatException e) {
            return 8;
        }
    }

    private String normalizeView(String s) {
        if (s == null || s.isBlank()) return "AMOUNT";
        s = s.trim().toUpperCase();
        return "RATIO".equals(s) ? "RATIO" : "AMOUNT";
    }

    private List<QuarterlyTrend> sliceLast(List<QuarterlyTrend> all, int n) {
        if (all == null || all.isEmpty()) return Collections.emptyList();
        if (all.size() <= n) return all;
        return new ArrayList<>(all.subList(all.size() - n, all.size()));
    }
}