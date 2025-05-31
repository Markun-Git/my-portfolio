<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>つながりDiaryへようこそ</title>
<link rel="stylesheet" href="CSS/style.css">
<link rel="stylesheet" href="CSS/style02.css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+JP&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=M+PLUS+Rounded+1c&display=swap" rel="stylesheet">
</head>
<body>
	<div class="overlay"></div>
	<div class="content">
		<h1>つながりDiaryへようこそ</h1>
		<%-- 退会完了メッセージ表示 --%>
		<% if (request.getAttribute("message") != null) { %>
			<p class="success-message"><%= request.getAttribute("message") %></p>
		<% } %>
		<form action="Login" method="post">
			<%-- ログイン失敗時のエラーメッセージ表示 --%>
			<% if (request.getAttribute("loginErrorMsg") != null) { %>
				<p class="error-message"><%= request.getAttribute("loginErrorMsg") %></p>
			<% } %>
			<label for="loginName">ログインネーム:</label> <input type="text"
				id="loginName" name="loginName" placeholder="英数字で入力"><br> <label for="pass">パスワード:</label>
			<input type="password" id="pass" name="pass"><br>
			<button type="submit">ログイン</button>
			<a href="register.jsp" class="button-link">新規登録</a>
		</form>
	</div>
</body>
</html>