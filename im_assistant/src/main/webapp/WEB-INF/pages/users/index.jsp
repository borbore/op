<%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>翌能微链后台管理系统</title> 
	<script type="text/javascript" src="./../lib/jquery.min.js"></script>
	<link type="text/css" rel="stylesheet" href="./../assets/css/bootstrap.min.css" />
	<link type="text/css" rel="stylesheet" href="./../assets/css/common.css" />
	<link type="text/css" rel="stylesheet" href="./../assets/css/main.css" />
<title>Insert title here</title>
</head>
<body>
<div class="menu">
	<h1 class="logo clearfix">
		<img src="./../assets/images/logo.png" alt="" /> <span><b>微链平台</b>微链管理后台</span>
	</h1>
	<ul class="menu-list">
		<li class="curr"><a href="./../views/enterprise.jsp" target="content_frame">订阅号管理</a></li>
		<li><a href="javascript;" target="content_frame">应用管理</a></li>
		<li>
			<a href="javascript;" target="content_frame">共轴运营管理</a>
			<div class="lever">
				<a href="javascript;" target="content_frame">共轴内容管理</a>
				<a href="./../views/coaxial.html" target="content_frame">共轴设置</a>
				<a href="javascript;" target="content_frame">商城</a>
				<a href="./../views/communicate.html" target="content_frame">通讯</a>
				<a href="javascript;" target="content_frame">社交</a>
				<a href="javascript;" target="content_frame">视频</a>
			</div>
		</li>
		<li>
			<a href="./../views/account.html" target="content_frame">平台管理</a>
			<div class="lever">
				<a href="./../views/account.html" target="content_frame">账号管理</a>
				<a href="javascript;" target="content_frame">用户管理</a>
			</div>
		</li>
	</ul>
	<div class="left-set clearfix">
		<ul class="user-list">
			<li><a href="#">个人资料</a></li>
			<li><a href="#">密码修改</a></li>
		</ul>
		<span class="user fl">admin</span>
		<a href="#" class="quit fr"></a>
		<a href="#" class="set fr"></a>
	</div>
</div>
	
<div class="main_box">
	<div class="main">
		<iframe width="100%" height="100%" frameborder="0" id="content_frame" name="content_frame" scrolling="auto" src="./../views/main.html" ></iframe>
	</div>
</div>
</body>
</html>
<script type="text/javascript">
	$(function(){
		$(".menu > ul > li > a").click(function(){
			$(this).parents().addClass("curr").siblings().removeClass("curr");
			$(this).parents().siblings().find(".lever").hide(300);
			$(this).siblings(".lever").toggle(300);
		});
		$(".lever > a").click(function(){
			$(this).addClass("curr1").siblings().removeClass("curr1");
		})


		//set
		$(".left-set .set").click(function(){
			$(this).toggleClass("cur-bg");
			$(".user-list").slideToggle();
		});
	})
</script>
