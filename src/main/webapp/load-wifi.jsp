<%@page import="api.ApiController"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>와이파이 정보 구하기</title>
	<style type="text/css">
		h1,a{
			display: flex;
   			justify-content: center;
		}
	</style>
</head>
<body>
	<%
		ApiController a = new ApiController();
		String str = a.getApi();
		System.out.println(str);
		
	%>
	<h1>
		<%
			out.print(str);
		%>
		개의 WIFI 정보를 정상적으로 저장하였습니다.
	</h1>
	<a href="index.jsp">
		홈으로 가기
	</a>	
</body>
</html>