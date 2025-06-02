<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>日記編集</title>
<link rel="stylesheet" href="CSS/style.css">
<link rel="stylesheet" href="CSS/style03.css">
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
	<h2>日記を編集</h2>
	<form action="EditMutterServlet" method="post"
		enctype="multipart/form-data">
		<input type="hidden" name="id" value="${diary.id}" />
		<p>
			タイトル：<input type="text" name="title" value="${diary.title}" size="40" maxlength="20" />
		</p>
		<p>本文：
			<textarea name="text" rows="8" cols="40" maxlength="10000">${diary.text}</textarea></p>
			<p style="font-size: 0.9em; color: #666; margin-top: 5px;">
                    YouTube動画を埋め込む場合は、動画の「共有」メニューから「埋め込み」を選び、<br>
                    表示されるコード（&lt;iframe&gt;...&lt;/iframe&gt;）をここに直接貼り付けてください。
                </p>
		<p>
			メイン画像（変更する場合のみ選択）： <input type="file" name="image" accept="image/*"
				onchange="previewImage(event, 'mainPreview')">
			<c:if test="${not empty diary.image}">
				<img src="ImageServlet?id=${diary.id}&type=main"
					style="max-width: 100px; max-height: 100px;"> 現在の画像
            </c:if>
			<img id="mainPreview" src="#" alt="メイン画像プレビュー"
				style="display: none; max-width: 200px; max-height: 200px;">
		</p>
		<input type="submit" value="更新" />
	</form>
	<br>
	<a href="DiaryDetailServlet?id=${diary.id}">詳細に戻る</a>
  </div>
</body>
</html>