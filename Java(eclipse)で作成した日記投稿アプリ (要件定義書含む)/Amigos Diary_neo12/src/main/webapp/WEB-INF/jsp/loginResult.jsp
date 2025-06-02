<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.User"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<%-- セッションスコープからユーザー情報を取得 --%>
<%
User loginUser = (User) session.getAttribute("loginUser");
String loginErrorMsg = (String) request.getAttribute("loginErrorMsg");
String loginErrorDetail = (String) request.getAttribute("loginErrorDetail"); // 詳細エラーメッセージを取得
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>つながりDiary ログイン結果</title>
<link rel="stylesheet" href="CSS/style.css">
<link rel="stylesheet" href="CSS/style08.css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+JP&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=M+PLUS+Rounded+1c&display=swap" rel="stylesheet">
<style>
    .button-container {
        margin-top: 20px;
    }

    .button-container a {
        margin-right: 10px;
    }
    
    .login-buttons {
    	display: flex;
    	justify-content: flex-start; /* これを flex-start に変更 */
    	gap: 15px;
    	margin-top: 30px;
	}
	
</style>
<script src="https://cdn.jsdelivr.net/npm/canvas-confetti@1.6.0/dist/confetti.browser.min.js"></script>
<script>
window.onload = function () {
	<c:if test="${loginUser != null}">
    confetti({
      particleCount: 150,
      spread: 80,
      origin: { y: 0.6 }
    });
  </c:if>
};
</script>
</head>
<body>
    <div class="overlay"></div>
    <div class="content">
        <h1>つながりDiaryログイン</h1>
        <% if (loginUser != null) { %>
            <p>ログインに成功しました</p>
            <p>ようこそ<%=loginUser.getUserName()%>さん</p>
            <div class="login-buttons">
                <a href="Main" class="button-link">日記投稿・閲覧へ</a>
                <a href="ProfileServlet" class="button-link">会員情報を確認</a>
            </div>
        <% } else if (loginErrorDetail != null) { %>
            <p class="error-message"><%=loginErrorDetail%></p>
            <a href="index.jsp" class="button-link">TOPへ戻る</a>
        <% } else if (loginErrorMsg != null) { %>
            <p class="error-message"><%=loginErrorMsg%></p>
            <a href="index.jsp" class="button-link">TOPへ戻る</a>
        <% } else { %>
            <p class="error-message">ログインに失敗しました</p>
            <a href="index.jsp" class="button-link">TOPへ戻る</a>
        <% } %>
    </div>
</body>
</html>