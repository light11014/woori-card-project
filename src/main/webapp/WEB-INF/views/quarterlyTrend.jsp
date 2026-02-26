<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="dev.sample.trend.QuarterlyTrend" %>

<%
    List<QuarterlyTrend> rows = (List<QuarterlyTrend>) request.getAttribute("rows");
    if (rows == null) rows = Collections.emptyList();

    // 기간 표시
    String fromQ = rows.isEmpty() ? "-" : rows.get(0).getQuarter();
    String toQ   = rows.isEmpty() ? "-" : rows.get(rows.size()-1).getQuarter();

    // 전체 합계(8개 카테고리 합)
    long grandTotal = 0;
    for (QuarterlyTrend r : rows) {
        grandTotal += (r.getFood() + r.getCar() + r.getTravelCulture() + r.getInsuranceHealth()
                + r.getEducationOffice() + r.getShopping() + r.getLiving() + r.getHome());
    }

    // 최근 분기 Top 카테고리
    String topName = "-";
    long topValue = -1;
    if (!rows.isEmpty()) {
        QuarterlyTrend last = rows.get(rows.size()-1);
        Map<String, Long> m = new LinkedHashMap<>();
        m.put("FOOD", last.getFood());
        m.put("CAR", last.getCar());
        m.put("TRAVEL/CULTURE", last.getTravelCulture());
        m.put("INSURANCE/HEALTH", last.getInsuranceHealth());
        m.put("EDU/OFFICE", last.getEducationOffice());
        m.put("SHOPPING", last.getShopping());
        m.put("LIVING", last.getLiving());
        m.put("HOME", last.getHome());

        for (Map.Entry<String, Long> e : m.entrySet()) {
            if (e.getValue() > topValue) {
                topValue = e.getValue();
                topName = e.getKey();
            }
        }
    }

    // 숫자 포맷
    java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quarterly Trend</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 24px; color: #111; }
        .header { display:flex; align-items: baseline; justify-content: space-between; gap: 16px; }
        .title { font-size: 26px; font-weight: 700; margin: 0; }
        .subtitle { margin: 6px 0 0; color: #555; }
        .cards { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 12px; margin: 18px 0; }
        .card { border: 1px solid #e5e5e5; border-radius: 10px; padding: 14px; background: #fafafa; }
        .card .k { color:#666; font-size: 12px; }
        .card .v { font-size: 18px; font-weight: 700; margin-top: 6px; }

        .table-wrap { border: 1px solid #e5e5e5; border-radius: 10px; overflow: hidden; }
        table { width: 100%; border-collapse: collapse; }
        thead th { position: sticky; top: 0; background: #f4f4f4; border-bottom: 1px solid #ddd; }
        th, td { padding: 10px 12px; border-bottom: 1px solid #eee; }
        th { text-align: left; font-weight: 700; }
        td.num, th.num { text-align: right; font-variant-numeric: tabular-nums; }
        tr:hover td { background: #fcfcff; }
        .delta { font-size: 12px; margin-left: 6px; }
        .up { color: #0a7a2f; }
        .down { color: #b00020; }
        .muted { color:#777; }

        @media (max-width: 980px) {
            .cards { grid-template-columns: 1fr; }
            body { margin: 14px; }
        }
    </style>
</head>
<body>

<div class="header">
    <div>
        <h1 class="title">Quarterly Trend</h1>
        <div class="subtitle">분기별 8개 카테고리 소비 금액 변화 추이</div>
    </div>
    <div class="muted">기간: <b><%= fromQ %></b> ~ <b><%= toQ %></b></div>
</div>

<div class="cards">
    <div class="card">
        <div class="k">분석 기간</div>
        <div class="v"><%= fromQ %> ~ <%= toQ %></div>
    </div>
    <div class="card">
        <div class="k">전체 소비 합계(8개 카테고리)</div>
        <div class="v"><%= nf.format(grandTotal) %></div>
    </div>
    <div class="card">
        <div class="k">최근 분기 Top 카테고리</div>
        <div class="v"><%= topName %> (<%= topValue < 0 ? "-" : nf.format(topValue) %>)</div>
    </div>
</div>

<div class="table-wrap">
<table>
    <thead>
        <tr>
            <th>quarter</th>
            <th class="num">food</th>
            <th class="num">car</th>
            <th class="num">travelCulture</th>
            <th class="num">insuranceHealth</th>
            <th class="num">educationOffice</th>
            <th class="num">shopping</th>
            <th class="num">living</th>
            <th class="num">home</th>
        </tr>
    </thead>
    <tbody>
    <%
        for (int i = 0; i < rows.size(); i++) {
            QuarterlyTrend r = rows.get(i);
            QuarterlyTrend prev = (i == 0) ? null : rows.get(i - 1);

            // 전분기 대비 변화율(각 컬럼)
            java.util.function.BiFunction<Long, Long, String> pct = (cur, pre) -> {
                if (pre == null || pre == 0) return "";
                double p = (cur - pre) * 100.0 / pre;
                String cls = (p > 0) ? "up" : (p < 0) ? "down" : "muted";
                return "<span class='delta " + cls + "'>(" + String.format("%+.1f%%", p) + ")</span>";
            };
    %>
        <tr>
            <td><%= r.getQuarter() %></td>

            <td class="num">
                <%= nf.format(r.getFood()) %>
                <%= (prev == null) ? "" : pct.apply(r.getFood(), prev.getFood()) %>
            </td>

            <td class="num">
                <%= nf.format(r.getCar()) %>
                <%= (prev == null) ? "" : pct.apply(r.getCar(), prev.getCar()) %>
            </td>

            <td class="num">
                <%= nf.format(r.getTravelCulture()) %>
                <%= (prev == null) ? "" : pct.apply(r.getTravelCulture(), prev.getTravelCulture()) %>
            </td>

            <td class="num">
                <%= nf.format(r.getInsuranceHealth()) %>
                <%= (prev == null) ? "" : pct.apply(r.getInsuranceHealth(), prev.getInsuranceHealth()) %>
            </td>

            <td class="num">
                <%= nf.format(r.getEducationOffice()) %>
                <%= (prev == null) ? "" : pct.apply(r.getEducationOffice(), prev.getEducationOffice()) %>
            </td>

            <td class="num">
                <%= nf.format(r.getShopping()) %>
                <%= (prev == null) ? "" : pct.apply(r.getShopping(), prev.getShopping()) %>
            </td>

            <td class="num">
                <%= nf.format(r.getLiving()) %>
                <%= (prev == null) ? "" : pct.apply(r.getLiving(), prev.getLiving()) %>
            </td>

            <td class="num">
                <%= nf.format(r.getHome()) %>
                <%= (prev == null) ? "" : pct.apply(r.getHome(), prev.getHome()) %>
            </td>
        </tr>
    <%
        }
    %>
    </tbody>
</table>
</div>

</body>
</html>