<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>메인 홈</title>

<style>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

html, body {
    height: 100%;
}

body {
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
}

.home-container {
    background: #fff;
    border-radius: 16px;
    padding: 48px 40px;
    width: 100%;
    max-width: 500px;
    box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
}

.home-header {
    text-align: center;
    margin-bottom: 36px;
}

.home-header h1 {
    font-size: 26px;
    font-weight: 700;
    color: #1a1a2e;
    margin-bottom: 8px;
}

.home-header p {
    color: #888;
    font-size: 14px;
}

.menu {
    margin-top: 20px;
}

.menu a {
    display: block;
    width: 100%;
    padding: 14px;
    margin-bottom: 14px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: #fff;
    text-decoration: none;
    border-radius: 8px;
    font-size: 15px;
    font-weight: 600;
    text-align: center;
    transition: opacity 0.2s, transform 0.1s;
}

.menu a:hover {
    opacity: 0.9;
}

.menu a:active {
    transform: scale(0.98);
}

.logout {
    text-align: center;
    margin-top: 24px;
}

.logout a {
    font-size: 13px;
    color: #666;
    text-decoration: none;
}
</style>
</head>
<body>

<div class="home-container">
    <div class="home-header">
        <h1>환영합니다 👋</h1>
        <p>
            <strong><%= session.getAttribute("loginUser") %></strong> 님,
            무엇을 확인하시겠습니까?
        </p>
    </div>

    <div class="menu">
        <a href="<%= request.getContextPath() %>/trend/quarterly">
            📊 분기별 소비 트렌드 보기
        </a>

        <!-- 확장 가능 -->
        <!-- <a href="#">📈 월별 통계 보기</a> -->
    </div>

    <div class="logout">
        <a href="<%= request.getContextPath() %>/logout">로그아웃</a>
    </div>
</div>

</body>
</html>