<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>コメント編集</title>
    <link rel="stylesheet" href="CSS/style.css">
    <link rel="stylesheet" href="CSS/style07.css">
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+JP&display=swap" rel="stylesheet">
	<link href="https://fonts.googleapis.com/css2?family=M+PLUS+Rounded+1c&display=swap" rel="stylesheet">
</head>
<body>
	<div class="overlay"></div>
    <div class="content">
    <h1>コメント編集</h1>
    <div class="form-container"> 	    <form action="EditCommentServlet" method="post">
	        <input type="hidden" name="commentId" value="${editComment.commentId}">
	        <input type="hidden" name="diaryId" value="${param.diaryId}">
	        <div class="form-group"> 	            <label for="commentText">コメント内容:</label>
	            <textarea id="commentText" name="commentText" rows="5" maxlength="500"><c:out value="${editComment.commentText}" /></textarea>
	        </div>
	        <div class="button-group"> 	            <button type="submit">更新</button>
	            <a href="DiaryDetailServlet?id=${param.diaryId}" class="button-link">詳細に戻る</a>
	        </div>
	    </form>
    </div> </div>
</body>
</html>