<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="CSS/style.css">
<link rel="stylesheet" href="CSS/style02.css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+JP&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=M+PLUS+Rounded+1c&display=swap" rel="stylesheet">

<script>
window.onload = function () {
  const canvas = document.getElementById('confetti-canvas');
  const ctx = canvas.getContext('2d');
  canvas.width = window.innerWidth;
  canvas.height = window.innerHeight;

  const confetti = [];
  const colors = ['#ff69b4', '#87cefa', '#90ee90', '#f0e68c', '#ffa07a'];

  for (let i = 0; i < 150; i++) {
    confetti.push({
      x: Math.random() * canvas.width,
      y: Math.random() * canvas.height - canvas.height,
      r: Math.random() * 6 + 4, // 半径
      d: Math.random() * 50 + 50, // 落下速度
      color: colors[Math.floor(Math.random() * colors.length)],
      tilt: Math.random() * 10 - 10
    });
  }

  function drawConfetti() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    confetti.forEach(c => {
      ctx.beginPath();
      ctx.fillStyle = c.color;
      ctx.ellipse(c.x, c.y, c.r, c.r / 2, c.tilt, 0, 2 * Math.PI);
      ctx.fill();
    });
    updateConfetti();
    requestAnimationFrame(drawConfetti);
  }

  function updateConfetti() {
    confetti.forEach(c => {
      c.y += c.d * 0.03;
      c.x += Math.sin(c.y * 0.03);
      if (c.y > canvas.height) {
        c.y = 0;
        c.x = Math.random() * canvas.width;
      }
    });
  }

  drawConfetti();
};
</script>

<title>つながりDiary</title>
</head>
<body>
	<canvas id="confetti-canvas"></canvas>
	<div class="overlay"></div>
    <div class="content">
	<h1>つながりDiaryログアウト</h1>
	<p>ログアウトしました</p>
	<a href="index.jsp" class="button-link">トップへ</a>
  </div>
</body>
</html>