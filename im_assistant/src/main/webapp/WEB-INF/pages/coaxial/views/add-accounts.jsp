<%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>翌能微链后台管理系统-订阅号管理</title>
	<link type="text/css" rel="stylesheet" href="<%=basePath%>css/common.css" />
	<link type="text/css" rel="stylesheet" href="<%=basePath%>css/main.css" />
</head>
<body>
<div class="container">
	<div class="tabs-nav">
		<ul class="clearfix">
			<li><a href="add">订阅号列表</a></li>
			<li class="active" >新增订阅号</li>
		</ul>
	</div>
	<!--新增订阅号-->
	<div class="tabs-box">
		<form action="adds">
		<ul class="add-enter">
			<li><span class="lab">订阅账号 :</span><input type="text"  name="username" /></li>
			<li><span class="lab">密码 :</span><input type="password" name="plainPassword" /></li>
			<li><span class="lab">标识码 :</span><input type="text" name="vCode" /></li>
			<li><span class="lab">订阅名称 :</span><input type="text" name="name"/></li>
			<li><span class="lab">邮箱 :</span><input type="text" name="email" /></li>
			<li><span class="lab">类型 :</span><input type="text"  name="type"/></li>
			<li><span class="lab">联系电话 :</span><input type="text" name="hotline" /></li>
			<li><span class="lab">订阅简介 :</span><textarea name="intro" ></textarea><span class="tip">限500个汉字以内</span></li>
		</ul>
		<div class="form-btn">
			<button type="submit" class="confirm">确定</button>
			<button  class="cancel">取消</button>
		</div>
		</form>
	</div>
</div>
<script type="text/javascript" src="<%=basePath%>js/jquery.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>js/plugs/laydate/laydate.js"></script>
<script type="text/javascript" src="<%=basePath%>js/plugs/jquery.validation.js"></script>
<script>
	$(document).ready(function() {
		$("input[name='username']").validation({rule:'required'} ,  '此项必填');
		$("input[name='plainPassword']").validation({rule:'required'} ,  '此项必填');
		//$("input[name='intro']").validation({rule:"maxlenght",required:true} ,  '此项必填','限50个汉字以内');
		$("input[name='vCode']").validation({rule:'required'} ,  '此项必填');
		$("input[name='name']").validation({rule:'required'} ,  '此项必填');
		$("input[name='email']").validation({rule:'required'} ,  '此项必填');
		//$("input[name='hotline']").validation({rule:'cellphone',required:true} ,  '请输入正确手机号');
	})
</script>

</body>
</html>