package dev.trend;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.springframework.web.context.WebApplicationContext; // 추가
import org.springframework.web.context.support.WebApplicationContextUtils; // 추가

@WebServlet("/trend/quarterly")
public class QuarterlyTrendServlet extends HttpServlet {

    private QuarterlyTrendService trendService;

    @Override
    public void init() throws ServletException {
        // 서블릿 초기화 시 스프링 컨테이너에서 빈을 꺼내옴
        WebApplicationContext context = WebApplicationContextUtils
                .getRequiredWebApplicationContext(getServletContext());
        this.trendService = context.getBean(QuarterlyTrendService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1) 파라미터 처리
        int range = parseRange(req.getParameter("range"));
        String view = normalizeView(req.getParameter("view"));

        // 2) 서비스 빈을 통해 데이터 조회 (직접 DAO/DataSource 접근 X)
        List<QuarterlyTrend> rows = trendService.getTrends(range);

        // 3) 뷰 전달
        req.setAttribute("rows", rows);
        req.setAttribute("range", range);
        req.setAttribute("view", view);

        req.getRequestDispatcher("/WEB-INF/views/quarterlyTrend.jsp").forward(req, resp);
    }

    private int parseRange(String s) {
        if (s == null) return 8;
        try {
            int n = Integer.parseInt(s);
            return (n == 1 || n == 4 || n == 8) ? n : 8;
        } catch (NumberFormatException e) { return 8; }
    }

    private String normalizeView(String s) {
        if (s == null || s.isBlank()) return "AMOUNT";
        s = s.trim().toUpperCase();
        return "RATIO".equals(s) ? "RATIO" : "AMOUNT";
    }
}