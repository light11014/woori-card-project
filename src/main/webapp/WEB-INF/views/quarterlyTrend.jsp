<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, dev.sample.trend.QuarterlyTrend" %>

<%
List<QuarterlyTrend> rows = (List<QuarterlyTrend>) request.getAttribute("rows");
if (rows == null) rows = Collections.emptyList();

Integer rangeObj = (Integer) request.getAttribute("range");
int range = (rangeObj == null) ? 8 : rangeObj;

String view = (String) request.getAttribute("view");
if (view == null) view = "AMOUNT";
final String finalView = view;

QuarterlyTrend last = rows.size() >= 1 ? rows.get(rows.size()-1) : null;
QuarterlyTrend prev = rows.size() >= 2 ? rows.get(rows.size()-2) : null;

java.text.NumberFormat nf = java.text.NumberFormat.getInstance();

// 최근/전분기 총합
long lastTotal = 0;
long prevTotal = 0;

if (last != null) {
    lastTotal =
        last.getFood()+last.getCar()+last.getTravelCulture()
      + last.getInsuranceHealth()+last.getEducationOffice()
      + last.getShopping()+last.getLiving()+last.getHome();
}

if (prev != null) {
    prevTotal =
        prev.getFood()+prev.getCar()+prev.getTravelCulture()
      + prev.getInsuranceHealth()+prev.getEducationOffice()
      + prev.getShopping()+prev.getLiving()+prev.getHome();
}

double totalDelta = 0;
if (prevTotal != 0) totalDelta = (lastTotal - prevTotal) * 100.0 / prevTotal;

// 기간 표시
String fromQ = rows.isEmpty() ? "-" : rows.get(0).getQuarter();
String toQ   = rows.isEmpty() ? "-" : rows.get(rows.size()-1).getQuarter();

// Top3
class CatVal { String label; long v; CatVal(String l,long v){label=l; this.v=v;} }
List<CatVal> top3 = new ArrayList<>();
if (last != null){
    top3.add(new CatVal("FOOD", last.getFood()));
    top3.add(new CatVal("CAR", last.getCar()));
    top3.add(new CatVal("TRAVEL/CULTURE", last.getTravelCulture()));
    top3.add(new CatVal("INSURANCE/HEALTH", last.getInsuranceHealth()));
    top3.add(new CatVal("EDU/OFFICE", last.getEducationOffice()));
    top3.add(new CatVal("SHOPPING", last.getShopping()));
    top3.add(new CatVal("LIVING", last.getLiving()));
    top3.add(new CatVal("HOME", last.getHome()));
    top3.sort((a,b)->Long.compare(b.v, a.v));
    if (top3.size() > 3) top3 = top3.subList(0, 3);
}
%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Quarterly Trend</title>

<style>
*{margin:0;padding:0;box-sizing:border-box;}
html, body{
  min-height:100%;
}
body{
  font-family:'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  /* ✅ flex 중앙정렬 제거 (잘림/배경끊김 원인) */
  display:block;
  padding:24px;
}

/* ✅ 컨테이너만 가운데 정렬 */
.shell{
  width:100%;
  max-width:1100px;
  margin:0 auto;
}

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
.nav a{
  color:#444;text-decoration:none;
  font-weight:700;font-size:13px;
  margin-left:14px;
}
.nav a:hover{color:#667eea;}

.card{
  background:#fff;
  border-radius:16px;
  padding:22px;
  box-shadow:0 20px 60px rgba(0,0,0,0.2);
  /* ✅ 화면 하단 여백 (끝에서 딱 끊겨보이는 느낌 완화) */
  margin-bottom:24px;
}

.header{
  display:flex;
  justify-content:space-between;
  align-items:flex-end;
  gap:12px;
}
.header h1{font-size:22px;font-weight:800;color:#1a1a2e;}
.sub{color:#777;font-size:13px;margin-top:6px;}
.period{color:#777;font-size:13px;}

.grid{
  display:grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap:12px;
  margin-top:14px;
}
.box{
  background:#fafafa;
  border:1px solid #eee;
  border-radius:12px;
  padding:14px;
}
.k{font-size:12px;color:#777;font-weight:700;}
.v{margin-top:6px;font-size:18px;font-weight:800;color:#1a1a2e;}

.formbox{
  margin-top:14px;
  border:1px solid #eee;
  border-radius:12px;
  padding:14px;
  background:#fff;
}

label{font-size:12px;color:#555;font-weight:700;margin-right:6px;}
select{
  padding:10px 12px;
  border:2px solid #e8e8e8;
  border-radius:10px;
  font-size:14px;
  outline:none;
  background:#fff;
}
select:focus{border-color:#667eea;}
.btn{
  padding:11px 14px;
  border:none;border-radius:10px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color:#fff;font-weight:800;
  cursor:pointer;
}
.btn:hover{opacity:0.92;}

.insights{
  display:grid;
  grid-template-columns: 1fr 1fr;
  gap:12px;
  margin-top:12px;
}
.insight{
  background:#fafafa;
  border:1px solid #eee;
  border-radius:12px;
  padding:14px;
}
.insight h3{font-size:14px;color:#1a1a2e;margin-bottom:8px;}
.row{
  display:flex;
  justify-content:space-between;
  padding:8px 0;
  border-bottom:1px solid #f0f0f0;
}
.row:last-child{border-bottom:none;}
.muted{color:#777;font-size:12px;}

.tablewrap{
  margin-top:14px;
  border:1px solid #eee;
  border-radius:12px;
  overflow:hidden;
  background:#fff;
}
table{width:100%;border-collapse:collapse;background:#fff;}
th,td{padding:10px 12px;border-bottom:1px solid #f0f0f0;}
th{background:#f8f9ff;text-align:left;font-size:12px;color:#555;}
td.num, th.num{text-align:right;font-variant-numeric: tabular-nums;}
.up{color:#0a7a2f;font-size:12px;font-weight:700;}
.down{color:#b00020;font-size:12px;font-weight:700;}

@media (max-width: 980px){
  body{padding:14px;}
  .grid{grid-template-columns:1fr;}
  .insights{grid-template-columns:1fr;}
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
      <div>
        <h1>분기별 소비 트렌드</h1>
        <div class="sub">분기별 8개 카테고리 소비 추이</div>
      </div>
      <div class="period">기간: <b><%= fromQ %></b> ~ <b><%= toQ %></b></div>
    </div>

    <div class="grid">
      <div class="box">
        <div class="k">최근 분기</div>
        <div class="v"><%= (last==null? "-" : last.getQuarter()) %></div>
      </div>
      <div class="box">
        <div class="k">최근 분기 총합</div>
        <div class="v"><%= nf.format(lastTotal) %></div>
      </div>
      <div class="box">
        <div class="k">전분기 대비(총합)</div>
        <div class="v" style="color:<%= totalDelta>=0 ? "#0a7a2f" : "#b00020" %>">
          <%= (prev==null ? "-" : String.format("%+.1f%%", totalDelta)) %>
        </div>
      </div>
      <div class="box">
        <div class="k">보기 모드</div>
        <div class="v"><%= "RATIO".equals(finalView) ? "비율(%)" : "금액" %></div>
      </div>
    </div>

    <div class="formbox">
      <form method="get" action="<%=request.getContextPath()%>/trend/quarterly"
            style="display:flex; gap:10px; flex-wrap:wrap; align-items:center;">
        <div>
          <label>기간</label>
          <select name="range">
            <option value="1" <%=range==1?"selected":""%>>최근 1개</option>
            <option value="4" <%=range==4?"selected":""%>>최근 4개</option>
            <option value="8" <%=range==8?"selected":""%>>최근 8개</option>
          </select>
        </div>

        <div>
          <label>표시</label>
          <select name="view">
            <option value="AMOUNT" <%= "AMOUNT".equals(finalView)?"selected":"" %>>금액</option>
            <option value="RATIO" <%= "RATIO".equals(finalView)?"selected":"" %>>비율(%)</option>
          </select>
        </div>

        <button class="btn" type="submit">적용</button>
      </form>
    </div>

    <div class="insights">
      <div class="insight">
        <h3>최근 분기 Top3</h3>
        <% if (top3.isEmpty()) { %>
          <div class="muted">데이터 없음</div>
        <% } else { for (int i=0;i<top3.size();i++){
             CatVal cv = top3.get(i);

             String text = nf.format(cv.v);
             if ("RATIO".equals(finalView)) {
               double pct = (lastTotal==0)?0.0:(cv.v*100.0/lastTotal);
               text = String.format("%.1f%%", pct);
             }
        %>
          <div class="row">
            <div><b><%= (i+1) + ". " + cv.label %></b></div>
            <div><%= text %></div>
          </div>
        <% } } %>
        <div class="muted">기준: <b><%= (last==null? "-" : last.getQuarter()) %></b></div>
      </div>

      <div class="insight">
        <h3>안내</h3>
        <div class="muted">테이블 괄호는 전분기 대비 증감률입니다.</div>
        <div class="muted">비율(%) 모드는 각 분기 총합 대비 비중입니다.</div>
      </div>
    </div>

    <div class="tablewrap">
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
        for (int i=0;i<rows.size();i++){
          QuarterlyTrend r = rows.get(i);
          QuarterlyTrend p = (i==0)?null:rows.get(i-1);

          long rowTotal =
              r.getFood()+r.getCar()+r.getTravelCulture()
            + r.getInsuranceHealth()+r.getEducationOffice()
            + r.getShopping()+r.getLiving()+r.getHome();

          long[] cur = new long[]{
            r.getFood(), r.getCar(), r.getTravelCulture(), r.getInsuranceHealth(),
            r.getEducationOffice(), r.getShopping(), r.getLiving(), r.getHome()
          };
          long[] pre = new long[]{
            p==null?0:p.getFood(),
            p==null?0:p.getCar(),
            p==null?0:p.getTravelCulture(),
            p==null?0:p.getInsuranceHealth(),
            p==null?0:p.getEducationOffice(),
            p==null?0:p.getShopping(),
            p==null?0:p.getLiving(),
            p==null?0:p.getHome()
          };
        %>
          <tr>
            <td><%= r.getQuarter() %></td>
            <%
              for(int c=0;c<cur.length;c++){
                String mainText;
                if ("RATIO".equals(finalView)) {
                  double pct = (rowTotal==0)?0.0:(cur[c]*100.0/rowTotal);
                  mainText = String.format("%.1f%%", pct);
                } else {
                  mainText = nf.format(cur[c]);
                }

                String deltaHtml = "";
                if (p != null && pre[c] != 0) {
                  double d = (cur[c]-pre[c])*100.0/pre[c];
                  String cls = d>=0 ? "up" : "down";
                  deltaHtml = " <span class='"+cls+"'>(" + String.format("%+.1f%%", d) + ")</span>";
                }
            %>
              <td class="num"><%= mainText %><%= deltaHtml %></td>
            <% } %>
          </tr>
        <% } %>
        </tbody>
      </table>
    </div>

  </div>
</div>
</body>
</html>