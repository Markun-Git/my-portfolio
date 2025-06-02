<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>検索結果</title>
<link rel="stylesheet" href="CSS/style.css">
<link rel="stylesheet" href="CSS/style07.css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+JP&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=M+PLUS+Rounded+1c&display=swap" rel="stylesheet">

</head>
<body>
	<div class="overlay"></div>
    <div class="content">
	<h1>検索結果</h1>
	<p>検索キーワード: <c:out value="${keyword}" /></p>
	<h2>検索された日記</h2>
	<c:choose>
		<c:when test="${not empty searchResults}">
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
					<c:forEach var="entry" items="${searchResults}">
						<tr>
							<td><c:if test="${not empty entry.userIconImage}">
									<img src="ImageServlet?id=${entry.userId}&type=icon" width="30"
										height="30" alt="アイコン" class="table-icon">
								</c:if></td>
							<td><c:out value="${entry.diaryTitle}" /></td>
							<td><c:out value="${entry.userName}" /></td>
							<td><c:out value="${entry.createdAt}" /></td>
							<td><a href="DiaryDetailServlet?id=${entry.diaryId}" class="view-button">見る</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:when>
		<c:otherwise>
			<p>該当する日記はありません。</p>
		</c:otherwise>
	</c:choose>
	<br>
	<a href="Main" class="button-link">一覧に戻る</a>
  </div>
</body>
</html>