<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="./others/lib/jquery.min.js"></script>
<script type="text/javascript">

$(function(){
    $("#send").click(function(){
        var jsondata = $("#jsondata").val();
        var jsonobj = JSON.parse(jsondata)
        var callback = function (data) {
            $("#result").html(JSON.stringify(data))
        }
        $.postJSON('user/test', jsonobj, callback)
        });
    })
 
    $.postJSON = function(url, data, callback) {
        return jQuery.ajax({
            'type' : 'POST',
            'url' : url,
            'contentType' : 'application/json',
            'data' : JSON.stringify(data),
            'dataType' : 'json',
            'success' : callback
        });
    };
</script>
</head>
<body>
    JSON对象
    <br>
    <textarea id="jsondata" cols="60" rows="5">
{"userid":2,"username":"admin"}
    </textarea><br>
 
    <button id="send">POST</button><br>
 
    <font color="red" id="result"></font>
</body>
</html>