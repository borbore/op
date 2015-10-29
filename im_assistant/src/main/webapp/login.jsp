<%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>用户登录-IM助手管理系统</title>
	<script type="text/javascript" src="./js/jquery.min.js"></script>
	<link type="text/css" rel="stylesheet" href="./css/common.css" />
	<link type="text/css" rel="stylesheet" href="./css/main.css" />
</head>
<body>
<div class="login_container">
	<div class="login_main">
		<div class="login_box">
			<div class="login_logo"><img src="./images/login_logo.png" alt="">IM助手管理系统</div>
			<div class="login_info_bd">
			 <form action="security/login" method="post"> 
				<div class="info">
					<h3>用户登录</h3>
					<ul class="info_list">
						<li class="usn"><input type="text" name="username"  placeholder="用户名" value="" autofocus><i>用户名或密码错误！</i></li>
						<li class="psd"><input type="password" name="password"  placeholder="密码" value=""><i style="display: none">请输入6位以上的数字或字母</i></li>
						<li class="yzm"><input type="yzm" placeholder="验证码" value=""><img src="./images/yzm-pic.gif"  /><i style="display: none">验证码错误</i></li>
					</ul>
					<p class="member"><a href="#">忘记密码？</a><input type="checkbox" checked />记住密码</p>
					<p class="info_btn"><button class="login_btn" onclick="javascript:window.location.href='account/index'">立即登录</button></p>
					 <p class="info_btn"><button class="login_btn" onclick="javascript:submit();">立即登录2</button></p> 
				</div>
			 </form> 
			</div>
		</div>
	</div>
</div>

</body>
</html>