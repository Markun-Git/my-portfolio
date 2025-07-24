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
        <h1>フォロー中ユーザー</h1>         <c:if test="${not empty followeeUsers}">
            <ul class="user-list"> <c:forEach var="user" items="${followeeUsers}">
            <li class="user-list-item">                     <c:if test="${not empty user.iconImage}">
                        <img src="UserImageServlet?id=${user.userId}" alt="アイコン" class="table-icon">
                    </c:if>
                    <c:if test="${empty user.iconImage}">
                        <img src="images/default_icon.png" alt="デフォルトアイコン" class="table-icon">                    
					 </c:if>
                    <a href="ProfileServlet?userId=${user.userId}">
                        <c:out value="${user.userName}" />
                    </a>
                </li>
            </c:forEach>
        </ul>
        </c:if>
        <c:if test="${empty followeeUsers}">
            <p>フォローしているユーザーはいません。</p>
        </c:if>
        <a href="Main" class="button-link">日記一覧に戻る</a>
    </div>
</body>
</html>
