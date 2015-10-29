<%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	System.out.print("main");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>IM助手管理系统</title> 
	<script type="text/javascript" src="<%=basePath%>js/jquery.min.js"></script>
	<link type="text/css" rel="stylesheet" href="<%=basePath%>css/bootstrap.min.css" />
	<link type="text/css" rel="stylesheet" href="<%=basePath%>css/common.css" />
	<link type="text/css" rel="stylesheet" href="<%=basePath%>css/main.css" />
<title>Insert title here</title>
</head>
<body>
<div class="container">
	<div class="index-con">
		<h2 class="user clearfix">
			<img src="<%=basePath%>images/user.png" alt="" />
			<span>风吹屁屁凉<em>超级管理员</em></span>
		</h2>
		<ul class="in-num clearfix">
			<li><span></span><b>58</b>待审批商品/条</li>
			<li><span></span><b>9514</b>VOIP电话量/分钟</li>
			<li><span></span><b>6541</b>总企业数/家</li>
			<li><span></span><b>65.2</b>总用户数/万</li>
		</ul>
		<div class="data-chart">
			<h1>近15天APP启动用户数</h1>
			<canvas id="canvasWeek" width="1260" height="230"></canvas>
		</div>
	</div>
</div>
<script src="<%=basePath%>js/plugs/chart.js"></script>
</body>
</html>