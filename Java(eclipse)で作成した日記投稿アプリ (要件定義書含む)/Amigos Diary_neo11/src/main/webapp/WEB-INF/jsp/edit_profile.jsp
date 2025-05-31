<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>会員情報編集</title>
  <link rel="stylesheet" href="CSS/style.css">
  <link rel="stylesheet" href="CSS/style04.css">
  <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+JP&display=swap" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css2?family=M+PLUS+Rounded+1c&display=swap" rel="stylesheet">
<script>
    function previewImage(event, previewId) {
        const input = event.target;
        const preview = document.getElementById(previewId);

        if (input.files && input.files[0]) {
            const reader = new FileReader();

            reader.onload = function(e) {
                preview.src = e.target.result;
                preview.style.display = 'block'; // プレビューを表示
            }

            reader.readAsDataURL(input.files[0]);
        } else {
            preview.src = '#';
            preview.style.display = 'none'; // プレビューを非表示
        }
    }
</script>  
</head>
<body>
  <div class="overlay"></div>
  <div class="content">
  <h1>会員情報編集</h1>
  <form action="EditProfileServlet" method="post" enctype="multipart/form-data">
    <label>ユーザー名: <input type="text" name="userName" value="${loginUser.userName}" required></label><br>
    <label>ログインネーム: <input type="text" name="loginName" value="${loginUser.loginName}" required></label><br>
    <label>新しいパスワード: <input type="password" name="newPassword"></label><br>
    <label>パスワード確認: <input type="password" name="confirmPassword"></label><br>
    <label>自己紹介: <input type="text" name="profileText" value="${loginUser.profileText}" maxlength="100"></label><br>
    <label>アイコン画像: <input type="file" name="iconImage" accept="image/*" onchange="previewImage(event, 'iconPreview')"></label><br>
    <c:if test="${not empty loginUser.iconImage}">
    	<img src="UserImageServlet?id=${loginUser.userId}" alt="現在のアイコン" style="max-width: 80px; max-height: 80px; border-radius: 50%;"> 現在のアイコン
	</c:if>
	<img id="iconPreview" src="#" alt="アイコンプレビュー" style="display: none; max-width: 100px; max-height: 100px; margin-top: 5px;">
    <button type="submit">更新</button>
    <a href="ProfileServlet" class="button-link">戻る</a>
  </form>
  <c:if test="${not empty editMsg}">
    <p class="success-message">${editMsg}</p>
  </c:if>
  <c:if test="${not empty editError}">
    <p class="error-message">${editError}</p>
  </c:if>
</div>
</body>
</html>
