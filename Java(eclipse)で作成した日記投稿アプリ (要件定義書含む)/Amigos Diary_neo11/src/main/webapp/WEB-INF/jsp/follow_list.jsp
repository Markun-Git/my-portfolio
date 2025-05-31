<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>お気に入りユーザー</title>
    <link rel="stylesheet" href="CSS/style.css">
    <link rel="stylesheet" href="CSS/style07.css">
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+JP&display=swap" rel="stylesheet">
	<link href="https://fonts.googleapis.com/css2?family=M+PLUS+Rounded+1c&display=swap" rel="stylesheet">
</head>
<body>
    <div class="overlay"></div>
    <div class="content">
        <h1>お気に入りユーザー（フォロー中）</h1>
        <ul>
            <c:forEach var="user" items="${followeeUsers}">
                <li>
                    <a href="ProfileServlet?userId=${user.userId}">
                        <c:out value="${user.userName}" />
                    </a>
                </li>
            </c:forEach>
        </ul>
        <a href="Main" class="button-link">日記一覧に戻る</a>
    </div>
</body>
</html>
