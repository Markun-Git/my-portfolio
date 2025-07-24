<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>つながりDiary会員登録</title>
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
		<h1>つながりDiary会員登録</h1>
		<%-- 登録成功メッセージ表示 --%>
		<%
		if (request.getAttribute("registerMsg") != null) {
		%>
		<p class="success-message"><%=request.getAttribute("registerMsg")%></p>
		<p>
			<a href="index.jsp" class="button-link">ログイン画面へ</a>
		</p>
		<%
		} else {
		%>
		<div class="form-container">
			<form action="RegisterServlet" method="post" enctype="multipart/form-data">
			<%-- 登録失敗メッセージ表示 --%>
			<%
			if (request.getAttribute("registerErrorMsg") != null) {
			%>
			<p class="error-message"><%=request.getAttribute("registerErrorMsg")%></p>
			<%
			}
			%>
			<div class="form-group"> 					<label for="userName">ユーザー名:</label>
					<input type="text" id="userName" name="userName" required placeholder="文字列で入力">
				</div>
				<div class="form-group"> 					<label for="loginName">ログインネーム:</label>
					<input type="text" id="loginName" name="loginName" required placeholder="英数字で入力">
				</div>
				<div class="form-group"> 					<label for="password">パスワード:</label>
					<input type="password" id="password" name="password" required>
				</div>
				<div class="form-group"> 					<label for="profileText">ひとことメッセージ:</label>
					<input type="text" id="profileText" name="profileText" maxlength="100">
				</div>
				<div class="form-group"> 					<label for="iconImage">アイコン画像:</label>
					<div> 						<input type="file" id="iconImage" name="iconImage" accept="image/*" onchange="previewImage(event, 'iconPreview')">
						<img id="iconPreview" src="#" alt="アイコンプレビュー" style="display: none; max-width: 100px; max-height: 100px; margin-top: 5px;">
					</div>
				</div>
				<div class="register-buttons"> 					<button type="submit">登録</button>
					<a href="index.jsp" class="button-link">ログイン画面へ戻る</a>
				</div>
			</form>
		</div> <%
		}
		%>
	</div>
</body>
</html>