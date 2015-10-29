<%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
			<li class="active"><a href="#">订阅号列表</a></li>
			<li ><a href="add-account">新增订阅号</a></li>
		</ul>
	</div>
	<!--企业列表-->
	<div class="tabs-box">
		<div class="search">
			<label>账号：</label><input type="text" name="username">
			<label>开通时间：</label>
			<input type="text" class="form-time" onclick="laydate()"> <span class="fontsize">—</span>
			<input type="text" class="form-time" onclick="laydate()">
			<label>状态：</label>
			<div class="select">
				<span class="default">全部</span>
				<ul>
					<li>禁用</li>
					<li>启用</li>
				</ul>
			</div>
			<button class="query-btn">查询</button>
		</div>
		<table class="table enter-table">
			<thead>
				<tr>
					<th>账号</th>
					<th>名称</th>
					<th>邮箱</th>
					<th>标识码</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
			
			<c:forEach var="account" items="${list }">
				<tr>
				<td>${account.username }</td>
				<td>${account.name }</td>
				<td>${account.email }</td>
				<td>${account.vCode }</td>
				<td><span class="red">禁用</span></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<!--分页-->
		<div class="page">
			<a href="#" class="pre"><<</a>
			<a href="#">1</a>
			<a href="#" class="page-cur">2</a>
			<a href="#">3</a>
			<a href="#">4</a>
			……
			<a href="#">12</a>
			<a href="#" class="next">>></a>
			<span>
				每页
				<select>
					<option>10</option>
					<option>20</option>
					<option>30</option>
					<option>40</option>
					<option>50</option>
				</select>
				条/总共94条记录
			</span>
		</div>
	</div>
</div>
<script type="text/javascript" src="<%=basePath%>js/jquery.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/plugs/laydate/laydate.js"></script>
<script type="text/javascript" src="<%=basePath%>js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>js/plugs/jquery.tablesorter.js"></script>
<script>
	$(function(){
		//第一列不进行排序(索引从0开始)
		//$.tablesorter.defaults.headers = {0: {sorter: false}};
		$(".table").tablesorter();
	});
</script>
</body>
</html>