<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>日記投稿</title>
<link rel="stylesheet" href="CSS/style.css">
<link rel="stylesheet" href="CSS/style03.css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+JP&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=M+PLUS+Rounded+1c&display=swap" rel="stylesheet">
<style>
/* スタイルは必要に応じて調整してください */
.post-form {
    margin: 20px;
    padding: 20px;
    border: 1px solid #ccc;
    background-color: #f9f9f9;
}
.form-group {
    margin-bottom: 15px; /* ここを調整して各グループ間にスペースを設ける */
}
.form-group label {
    display: block; /* ラベルをブロック要素にして、その後に改行されるようにする */
    margin-bottom: 5px; /* ラベルと入力欄の間のスペース */
    font-weight: bold;
}
.form-group input[type="text"],
.form-group textarea {
    /* width: 100%; */
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 4px;
    box-sizing: border-box; /* paddingを含めて幅を計算 */
}
.form-group textarea {
    height: 150px;
    resize: vertical;
    width: 350px;
}
.error-message {
    color: red;
    margin-bottom: 10px;
}
.button-group {
    margin-top: 20px;
    text-align: center;
}
.button-group button,
.button-group a.button-link {
    padding: 10px 20px;
    margin: 0 10px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    text-decoration: none;
    display: inline-block;
}
.button-group button {
    background-color: #007bff;
    color: white;
}
.button-group button:hover {
    background-color: #0056b3;
}
.button-group a.button-link {
    background-color: #6c757d;
    color: white;
}
.button-group a.button-link:hover {
    background-color: #545b62;
}
</style>
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
	<h1>新しい日記を投稿</h1>
	<div class="form-container">
		<form action="Main" method="post" enctype="multipart/form-data">
			<div class="form-group">
				<label for="title">タイトル：</label>
				<input type="text" id="title" name="title" maxlength="20">
			</div>
			<div class="form-group">
				<label for="text">本文：</label>
				<div>
					<textarea id="text" name="text" rows="8" maxlength="10000"></textarea>
					<p style="font-size: 0.9em; color: #666; margin-top: 5px;">
                    YouTube動画を埋め込む場合は、<br>動画の「共有」メニューから「埋め込み」を選び、<br>
                    表示されるコード（&lt;iframe&gt;...&lt;/iframe&gt;）を<br>ここに直接貼り付けてください。
                </p>
				</div>
			</div>
			<div class="form-group">
				<label for="image">画像（任意）：</label>
				<div>
					<input type="file" id="image" name="image" accept="image/*" onchange="previewImage(event, 'mainPreview')">
					<img id="mainPreview" src="#" alt="メイン画像プレビュー"
						style="display: none; max-width: 200px; max-height: 200px;">
				</div>
			</div>
			<div class="button-group">
				<button type="submit">投稿</button>
				<a href="Main" class="button-link">一覧に戻る</a>
			</div>
		</form>
	</div>
  </div>
</body>
</html>