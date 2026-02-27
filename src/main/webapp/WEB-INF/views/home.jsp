<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, dev.sample.trend.QuarterlyTrend" %>

<%
List<QuarterlyTrend> rows = (List<QuarterlyTrend>) request.getAttribute("rows");
if (rows == null) rows = Collections.emptyList();

QuarterlyTrend last = rows.size() >= 1 ? rows.get(rows.size()-1) : null;
QuarterlyTrend prev = rows.size() >= 2 ? rows.get(rows.size()-2) : null;

long lastTotal = 0, prevTotal = 0;
if (last != null) {
    lastTotal = last.getFood()+last.getCar()+last.getTravelCulture()
      + last.getInsuranceHealth()+last.getEducationOffice()
      + last.getShopping()+last.getLiving()+last.getHome();
}
if (prev != null) {
    prevTotal = prev.getFood()+prev.getCar()+prev.getTravelCulture()
      + prev.getInsuranceHealth()+prev.getEducationOffice()
      + prev.getShopping()+prev.getLiving()+prev.getHome();
}

double delta = 0;
if (prevTotal != 0) delta = (lastTotal - prevTotal) * 100.0 / prevTotal;

java.text.NumberFormat nf = java.text.NumberFormat.getInstance();

// 최근 분기 Top3
class CatVal { String label; long v; CatVal(String l,long v){this.label=l; this.v=v;} }
List<CatVal> top = new ArrayList<>();
if (last != null) {
    top.add(new CatVal("FOOD", last.getFood()));
    top.add(new CatVal("CAR", last.getCar()));
    top.add(new CatVal("TRAVEL/CULTURE", last.getTravelCulture()));
    top.add(new CatVal("INS/HEALTH", last.getInsuranceHealth()));
    top.add(new CatVal("EDU/OFFICE", last.getEducationOffice()));
    top.add(new CatVal("SHOPPING", last.getShopping()));
    top.add(new CatVal("LIVING", last.getLiving()));
    top.add(new CatVal("HOME", last.getHome()));
    top.sort((a,b)->Long.compare(b.v, a.v));
}
%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Dashboard</title>

<style>
*{margin:0;padding:0;box-sizing:border-box;}
html,body{height:100%;}
body{
  font-family:'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  min-height:100vh;
  display:flex;
  align-items:center;
  justify-content:center;
  padding:24px;
}
.shell{width:100%;max-width:980px;}
.nav{
  background:#fff;
  border-radius:16px;
  padding:14px 18px;
  box-shadow:0 20px 60px rgba(0,0,0,0.2);
  display:flex;
  justify-content:space-between;
  align-items:center;
  margin-bottom:14px;
}
.brand{font-weight:800;color:#1a1a2e;}
.nav a{color:#444;text-decoration:none;font-weight:700;font-size:13px;margin-left:14px;}
.nav a:hover{color:#667eea;}

.card{
  background:#fff;
  border-radius:16px;
  padding:22px;
  box-shadow:0 20px 60px rgba(0,0,0,0.2);
}
.header h1{font-size:22px;font-weight:800;color:#1a1a2e;margin-bottom:6px;}
.header p{font-size:13px;color:#777;}

.summary{
  display:grid;
  grid-template-columns: 1fr 1fr 2fr;
  gap:12px;
  margin-top:16px;
}
.box{
  background:#fafafa;
  border:1px solid #eee;
  border-radius:12px;
  padding:14px;
}
.k{font-size:12px;color:#777;font-weight:700;}
.v{margin-top:6px;font-size:18px;font-weight:800;color:#1a1a2e;}
.topline{margin-top:6px;color:#444;font-size:13px;}
.tag{
  display:inline-block;
  padding:2px 10px;
  border-radius:999px;
  background:#f2f4ff;
  color:#667eea;
  font-weight:800;
  font-size:12px;
  margin-right:6px;
}

.btn{
  display:inline-block;
  margin-top:14px;
  padding:10px 14px;
  border-radius:10px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color:#fff;
  text-decoration:none;
  font-weight:800;
  font-size:13px;
}
.btn:hover{opacity:0.92;}

@media (max-width: 980px){
  .summary{grid-template-columns:1fr;}
}
</style>
</head>

<body>
<div class="shell">
  <div class="nav">
    <div class="brand">Woori Card Analytics</div>
    <div>
      <a href="<%=request.getContextPath()%>/home">Dashboard</a>
      <a href="<%=request.getContextPath()%>/trend/quarterly">Trend</a>
      <a href="<%=request.getContextPath()%>/logout">Logout</a>
    </div>
  </div>

  <div class="card">
    <div class="header">
      <h1>환영합니다</h1>
      <p><b><%= session.getAttribute("loginUser") %></b> 님, 최근 소비 요약을 확인하세요.</p>
    </div>

    <div class="summary">
      <div class="box">
        <div class="k">최근 분기 총 소비</div>
        <div class="v"><%= nf.format(lastTotal) %></div>
      </div>

      <div class="box">
        <div class="k">전분기 대비(총합)</div>
        <div class="v" style="color:<%= delta>=0 ? "#0a7a2f" : "#b00020" %>">
          <%= (prev==null ? "-" : String.format("%+.1f%%", delta)) %>
        </div>
      </div>

      <div class="box">
        <div class="k">최근 분기 Top3 카테고리</div>
        <div class="topline">
          <% for(int i=0;i<Math.min(3, top.size()); i++){ %>
            <span class="tag"><%= (i+1) + ". " + top.get(i).label %></span>
          <% } %>
        </div>
      </div>
    </div>

    <a class="btn" href="<%=request.getContextPath()%>/trend/quarterly">상세 분석 보기 →</a>
  </div>
</div>
</body>
</html>