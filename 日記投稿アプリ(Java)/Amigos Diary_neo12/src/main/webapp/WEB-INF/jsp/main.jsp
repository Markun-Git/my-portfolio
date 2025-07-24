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
.error-message {
	color: red;
	margin-top: 10px;
}
.search-message {
    color: orange;
    margin-bottom: 10px;
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
					<td> <%-- アイコン表示 --%>
                        <c:if test="${not empty diary.iconImage}">
                            <img src="UserImageServlet?id=${diary.userId}" alt="アイコン" class="table-icon"> <%-- ★MutterモデルのuserIdを使用 --%>
                        </c:if>
                        <c:if test="${empty diary.iconImage}">
                            <img src="images/default_icon.png" alt="デフォルトアイコン" class="table-icon">
                       </c:if>
                    </td>
					<td><c:out value="${diary.title}" /></td>
					<td> <%-- 投稿者名ボタン --%>
						<c:set var="isCurrentUser" value="${diary.userId eq loginUser.userId}" /> <%-- ログインユーザーと投稿者が同じか判定 --%>
						<a href="ProfileServlet?userId=${diary.userId}"
						   class="poster-button <c:if test="${isCurrentUser}">current-user</c:if>">
							<c:out value="${diary.userName}" />
						</a>
					</td>
					<td><c:out value="${diary.createdAt}" /></td>
					<td><a href="DiaryDetailServlet?id=${diary.id}" class="view-button">見る</a></td> <%-- ★Mutterモデルのidを使用 --%>
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