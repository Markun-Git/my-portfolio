<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>日記詳細</title>
    <link rel="stylesheet" href="CSS/style.css">
    <link rel="stylesheet" href="CSS/style06.css">
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+JP&display=swap" rel="stylesheet">
	<link href="https://fonts.googleapis.com/css2?family=M+PLUS+Rounded+1c&display=swap" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script>
        function confirmDeletion(deleteUrl) {
            Swal.fire({
                title: '本当に削除してもよろしいですか？',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'はい',
                cancelButtonText: 'いいえ',
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.href = deleteUrl;
                }
            });
            return false;
        }

        function toggleLike(diaryId, isLiked) {
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = 'LikeServlet';

            const diaryIdInput = document.createElement('input');
            diaryIdInput.type = 'hidden';
            diaryIdInput.name = 'diaryId';
            diaryIdInput.value = diaryId;

            const actionInput = document.createElement('input');
            actionInput.type = 'hidden';
            actionInput.name = 'action';
            actionInput.value = isLiked ? 'unlike' : 'like';

            form.appendChild(diaryIdInput);
            form.appendChild(actionInput);
            document.body.appendChild(form);
            form.submit();
        }
    </script>
    <style>
        /* ... 既存のスタイル ... */
        .like-button {
            border: 2px solid #007bff;
            color: #007bff;
            background-color: white;
            border-radius: 20px;
            padding: 5px 10px;
            font-size: 0.9em;
            cursor: pointer;
            margin-left: 10px;
            display: inline-flex;
            align-items: center;
        }

        .like-button.liked {
            background-color: #007bff;
            color: white;
        }

        .like-count {
            margin-left: 5px;
            font-size: 0.9em;
        }
    </style>
</head>
<body>
	<div class="overlay"></div>
  	<div class="content fade-in">
    <h1>日記詳細</h1>
    <p>
        タイトル：
        <c:out value="${diary.title}" />
    </p>
    <p>
        投稿者：
        <c:out value="${diary.userName}" />
    </p>
    <p>
        投稿日時：
        <c:out value="${diary.createdAt}" />
    </p>
    <p>本文：<pre><c:out value="${diary.text}" escapeXml="false" /></pre></p>
    <c:if test="${not empty diary.image}">
        <img src="ImageServlet?id=${diary.id}&type=main"
             style="max-width: 300px;">
    </c:if>
    <br>
    <div style="display: flex; align-items: center; margin-bottom: 10px;">
        <c:if test="${diary.userName == loginUser.userName}">
            <a href="EditFormServlet?id=${diary.id}" style="margin-right: 10px;">編集</a>
            <a href="#" onclick="return confirmDeletion('DeleteMutterServlet?id=${diary.id}');" style="margin-right: 10px;">削除</a>
        </c:if>
        <button class="like-button ${isLiked ? 'liked' : ''}" onclick="toggleLike('${diary.id}', ${isLiked})">
            いいね！👍
        </button>
        <span class="like-count">${likeCount}</span>
        <a href="Main" class="button-link" style="margin-left: 160px;">一覧に戻る</a>
    </div>
    <div class="comment-container">
        <c:if test="${not empty commentErrorMsg}">
            <p class="error-message"><c:out value="${commentErrorMsg}" /></p>
        </c:if>
        <c:if test="${not empty loginUser}">
            <div class="comment-form">
                <form action="PostCommentServlet" method="post" style="display: flex; align-items: center;">
                    <input type="hidden" name="diaryId" value="${diary.id}">
                    <textarea name="commentText" placeholder="コメントを追加" cols="68" rows="1"></textarea>
                    <button type="submit" class="button-link">送信</button>
                </form>
            </div>
        </c:if>
        <ul class="comment-list">
            <c:forEach var="comment" items="${commentList}">
                <li class="comment-item">
                    <div class="comment-body">
                        <div class="comment-info">
                            <span><c:out value="${comment.userName}" /></span>
                            <span><c:out value="${comment.createdAt}" /></span>
                        </div>
                        <p class="comment-text"><pre><c:out value="${comment.commentText}" /></pre></p>
                        <c:if test="${loginUser.userId == comment.userId}">
                            <div class="comment-actions">
                                <a href="EditCommentServlet?commentId=${comment.commentId}&diaryId=${diary.id}" style="margin-right: 5px;">編集</a>
                                <a href="#" onclick="return confirmDeletion('DeleteCommentServlet?commentId=${comment.commentId}&diaryId=${diary.id}');">削除</a>
                            </div>
                        </c:if>
                    </div>
                </li>
            </c:forEach>
         </ul>
       </div>
      </div>
    <br>
</body>
</html>