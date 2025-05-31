<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>会員情報</title>
    <link rel="stylesheet" href="CSS/style.css">
    <link rel="stylesheet" href="CSS/style03.css">
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+JP&display=swap" rel="stylesheet">
	<link href="https://fonts.googleapis.com/css2?family=M+PLUS+Rounded+1c&display=swap" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script>
        function confirmDeletion() {
            Swal.fire({
                title: '本当に削除しますか？',
                text: "会員情報を削除すると、これまでの投稿やコメントなどのデータはすべて削除されます。",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'はい、削除します',
                cancelButtonText: 'いいえ、削除しません',
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.href = 'DeleteUserServlet';
                }
            });
            return false;
        }
    </script>
    <style>
        .profile-container {
            margin: 20px auto;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
            width: 400px;
        }

        .profile-item {
            margin-bottom: 10px;
        }

        .profile-label {
            font-weight: bold;
            display: inline-block;
            width: 120px;
        }

        .profile-value {
            display: inline-block;
        }

        .button-container {
            margin-top: 20px;
            display: flex;
            justify-content: space-around;
        }

        .message {
            color: green;
            margin-bottom: 10px;
        }

        .error-message {
            color: red;
            margin-bottom: 10px;
        }
        
        .danger-button {
  			background-color: #e53935;
		    color: white;
		    border: none;
		    padding: 8px 16px;
		    border-radius: 4px;
		    cursor: pointer;
		    margin-top: 12px;
		}
		    
		.danger-button:hover {
  			background-color: #c62828;
		}
		
		.button {
  			display: inline-block;
  			padding: 10px 12px;
  			border: 1px solid #28a745;
  			color: #28a745;
  			text-decoration: none;
  			border-radius: 5px;
  			font-size: 15px;
  			font-weight: bold; 
 			background-color: transparent;
  			cursor: pointer;
  			transition: background-color 0.3s, color 0.3s;
  			margin-top: 8px;
		}

		.button:hover {
 			background-color: #28a745;
  			color: white;
		}
		
		.diary-text-content {
    		white-space: pre-wrap; /* 改行とスペースを維持しつつ、必要に応じて折り返す */
    		word-wrap: break-word; /* 長い単語でも折り返す */
   			 /* min-height など、不要な高さを強制するCSSがあれば削除または調整 */
   			overflow: hidden; /* 内容がはみ出す場合にスクロールバーを表示しないなど */
		}

		/* 動画の埋め込みがはみ出さないように */
		.diary-text-content iframe {
    		max-width: 100%; /* 親要素の幅に合わせて最大幅を設定 */
    		height: auto; /* アスペクト比を維持 */
    		display: block; /* iframeをブロック要素にして、前後の要素と改行させる */
    		margin: 10px 0; /* 必要に応じて上下の余白 */
		}
		
    </style>
</head>
<body>
	<div class="overlay"></div>
    <div class="content">
    <div class="container fade-in">
        <h1>会員情報</h1>
        <%-- ここに追加！ --%>
    	<c:if test="${not empty followMsg}">
      		<p class="success-message"><c:out value="${followMsg}" /></p>
    	</c:if>
        
        <%-- 退会成功時のメッセージ表示 --%>
        <c:if test="${not empty message}">
            <p class="message"><c:out value="${message}" /></p>
        </c:if>
        <%-- 退会失敗時のエラーメッセージ表示 --%>
        <c:if test="${not empty error}">
            <p class="error-message"><c:out value="${error}" /></p>
        </c:if>
        <div class="profile-container">
            <div class="profile-item">
                <span class="profile-label">ユーザー名:</span>
                <span class="profile-value"><c:out value="${viewUser.userName}" /></span>
            </div>
            <div class="profile-item">
                <span class="profile-label">ログインネーム:</span>
                <span class="profile-value"><c:out value="${viewUser.loginName}" /></span>
            </div>
            <div class="profile-item">
                <span class="profile-label">パスワード:</span>
                <span class="profile-value">********</span> 
            </div>
        	<div class="profile-item">
        		<span class="profile-label">自己紹介:</span>
        		<span class="profile-value"><c:out value="${viewUser.profileText}" /></span>
    		</div>
    		<div class="profile-item">
        		<span class="profile-label"></span>
       			 <c:if test="${not empty viewUser.iconImage}">
            		<img src="UserImageServlet?id=${viewUser.userId}" alt="アイコン" width="80" height="80" style="border-radius: 50%;">
        </c:if>
    </div>    
</div>
<div class="button-container">
    <c:choose>
        <c:when test="${isSelf}">
            <form action="DeleteUserServlet" method="post" style="display:inline;">
                <button type="submit" class="danger-button" onclick="return confirmDeletion();">会員情報を削除</button>
            </form>
            <a href="EditProfileServlet" class="button-link">会員情報を編集</a>
            <a href="Main" class="button-link">日記一覧へ</a>
        </c:when>
        <c:otherwise>
            <form action="FollowServlet" method="post" style="display:inline;">
                <input type="hidden" name="followeeId" value="${viewUser.userId}" />
                <input type="hidden" name="action" value="${isFollowing ? 'unfollow' : 'follow'}" />
                <button type="submit" class="button">${isFollowing ? 'フォロー解除' : 'フォロー'}</button>
            </form>
            <a href="Main" class="button-link">日記一覧へ</a>
        </c:otherwise>
    </c:choose>
</div>

 <!-- 下にそのユーザーの日記一覧を表示する場合 -->
<div class="diary-list">
    <h2>このユーザーの日記一覧</h2>
    <table>
        <thead>
            <tr>
                <th>タイトル</th>
                <th>本文</th>
                <th>日時</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="diary" items="${mutterList}">
                <tr>
                    <td><c:out value="${diary.title}" /></td>
                    <td><div class="diary-text-content"><c:out value="${diary.text}" escapeXml="false" /></div></td>
                    <td><c:out value="${diary.createdAt}" /></td>
                    <td><a href="DiaryDetailServlet?id=${diary.id}">見る</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
</div>
</body>
</html>