package dev.sample.trend;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import dev.sample.ApplicationContextListener;

@WebServlet("/trend/quarterly")
public class QuarterlyTrendServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        ServletContext ctx = getServletContext();
        DataSource ds = ApplicationContextListener.getReadDataSource(ctx);

        QuarterlyTrendDao dao = new JdbcQuarterlyTrendDao(ds);
        List<QuarterlyTrend> rows = dao.findAllQuarterlyTrends();

        out.println("<html><head><meta charset='UTF-8'></head><body>");
        out.println("<h3>Quarterly Trend</h3>");

        out.println("<table border='1' cellpadding='6' cellspacing='0'>");
        out.println("<tr>"
                + "<th>quarter</th>"
                + "<th>food</th>"
                + "<th>car</th>"
                + "<th>travelCulture</th>"
                + "<th>insuranceHealth</th>"
                + "<th>educationOffice</th>"
                + "<th>shopping</th>"
                + "<th>living</th>"
                + "<th>home</th>"
                + "</tr>");

        for (QuarterlyTrend r : rows) {
            out.println("<tr>"
                    + "<td>" + esc(r.getQuarter()) + "</td>"
                    + "<td>" + fmt(r.getFood()) + "</td>"
                    + "<td>" + fmt(r.getCar()) + "</td>"
                    + "<td>" + fmt(r.getTravelCulture()) + "</td>"
                    + "<td>" + fmt(r.getInsuranceHealth()) + "</td>"
                    + "<td>" + fmt(r.getEducationOffice()) + "</td>"
                    + "<td>" + fmt(r.getShopping()) + "</td>"
                    + "<td>" + fmt(r.getLiving()) + "</td>"
                    + "<td>" + fmt(r.getHome()) + "</td>"
                    + "</tr>");
        }

        out.println("</table>");
        out.println("</body></html>");
    }

    private static String fmt(long v) {
        return String.format("%,d", v);
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}