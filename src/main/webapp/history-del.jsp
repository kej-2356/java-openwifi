<%@page import="api.ApiController"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>와이파이 정보 구하기</title>
</head>
<body>
<h1>삭제 화면</h1>
<%
	String id = request.getParameter("id");
	
	ApiController api = new ApiController();	
	api.delHistory(id);
	
	response.sendRedirect("history.jsp");
%>
</body>
</html>