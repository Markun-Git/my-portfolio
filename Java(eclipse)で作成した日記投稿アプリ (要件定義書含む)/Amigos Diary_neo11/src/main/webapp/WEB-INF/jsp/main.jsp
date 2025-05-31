<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>日記一覧</title>
<link rel="stylesheet" href="CSS/style.css">
<link rel="stylesheet" href="CSS/style06.css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+JP&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=M+PLUS+Rounded+1c&display=swap" rel="stylesheet">
<style>
/* 既存のスタイルはそのまま */
table {
	border-collapse: collapse;
	width: 600px;
}

th, td {
	border: 1px solid #ccc;
	padding: 8px;
	text-align: left;
}

.post-form {
	margin-bottom: 20px;
	padding: 15px;
	border: 1px solid #eee;
	background-color: #f9f9f9;
}

.form-group {
	margin-bottom: 10px;
}

.form-group label {
	display: inline-block;
	width: 80px;
	text-align: left;
}

.error-message {
	color: red;
	margin-top: 10px;
}
.search-message {
    color: orange;
    margin-bottom: 10px;
}

/* ページネーションのスタイル (前回と同じ、そのまま追加) */
.pagination {
    margin-top: 20px;
    text-align: center;
}
.pagination a {
    display: inline-block;
    padding: 8px 12px;
    margin: 0 4px;
    border: 1px solid #007bff;
    border-radius: 5px;
    text-decoration: none;
    color: #007bff;
    background-color: #fff;
}
.pagination a:hover {
    background-color: #007bff;
    color: #fff;
}
.pagination span.current-page {
    display: inline-block;
    padding: 8px 12px;
    margin: 0 4px;
    border: 1px solid #0056b3;
    border-radius: 5px;
    text-decoration: none;
    background-color: #0056b3;
    color: #fff;
    font-weight: bold;
    cursor: default;
}

/* 投稿者名のボタンの基本スタイル */
.poster-button {
    display: inline-block;
    padding: 5px 10px;
    border: 1px solid #ccc;
    background-color: #f0f0f0;
    color: #333;
    text-decoration: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 0.9em;
    text-align: center;
    min-width: 60px;
}

/* ログインユーザーの投稿者名ボタンのスタイル (前回指示のa.poster-button.current-userで) */
a.poster-button.current-user {
    background-color: #8A2BE2; /* 紫色 */
    color: #fff;
    border-color: #6A1EB2;
    font-weight: bold;
}
</style>
</head>
<body>
	<div class="overlay"></div>
    <div class="content fade-in">
	<h1>日記一覧</h1>
	<p>
		<c:out value="${loginUser.userName }" />
		さん、ログイン中 <a href="Logout">ログアウト</a> <a href="DiaryFormServlet">日記を追加</a>
		<a href="FollowListServlet" class="button-link">お気に入り</a>
		<form action="SearchServlet" method="get" style="display: inline-block; margin-left: 10px;">
		    <input type="text" name="keyword" placeholder="キーワード検索" size="20">
		    <button type="submit" class="button-link" style="padding: 5px 10px;">検索</button>
		</form>
	</p>
	<c:if test="${not empty errorMsg}">
		<p class="error-message">
			<c:out value="${errorMsg}" />
		</p>
	</c:if>
	<c:if test="${not empty searchMessage}">
        <p class="search-message"><c:out value="${searchMessage}" /></p>
    </c:if>
	<h2>投稿された日記</h2>
	<table>
		<thead>
			<tr>
				<th>アイコン</th>
				<th>タイトル</th>
				<th>投稿者</th>
				<th>日時</th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="diary" items="${mutterList}">
				<tr>
					<td>
                        <c:if test="${not empty diary.iconImage}">
                            <img src="ImageServlet?id=${diary.userId}&type=icon" width="30" height="30"> <%-- ★MutterモデルのuserIdを使用 --%>
                        </c:if>
                    </td>
					<td><c:out value="${diary.title}" /></td>
					<td>
						<%-- インラインスタイルを削除し、クラスベースに戻す --%>
						<c:set var="posterUserName" value="${diary.userName}" /> <%-- MutterモデルのuserNameを直接参照 --%>
						<c:set var="isCurrentUser" value="${posterUserName eq loginUser.userName}" />
						<a href="ProfileServlet?userId=${diary.userId}" <%-- ★MutterモデルのuserIdを使用 --%>
						   class="poster-button <c:if test="${isCurrentUser}">current-user</c:if>">
							<c:out value="${posterUserName}" />
						</a>
					</td>
					<td><c:out value="${diary.createdAt}" /></td>
					<td><a href="DiaryDetailServlet?id=${diary.id}">見る</a></td> <%-- ★Mutterモデルのidを使用 --%>
				</tr>
			</c:forEach>
		</tbody>
	</table>

    <%-- ページネーション表示部分 --%>
    <div class="pagination">
        <c:if test="${paginationInfo.totalPages > 1}">
            <%-- 各ページ番号のリンクを生成 --%>
            <c:forEach begin="1" end="${paginationInfo.totalPages}" var="pageNo">
                <c:choose>
                    <c:when test="${pageNo == paginationInfo.currentPage}">
                        <span class="current-page"><c:out value="${pageNo}" /></span>
                    </c:when>
                    <c:otherwise>
                        <a href="Main?page=<c:out value="${pageNo}" />"><c:out value="${pageNo}" /></a> <%-- ★Mainサーブレットにリンク --%>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <span>&nbsp;→&nbsp;各ページに<c:out value="${paginationInfo.recordsPerPage}" />件ずつ表示</span>
        </c:if>
    </div>
  </div>
</body>
</html>