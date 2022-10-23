<%@page import="api.ApiController"%>
<%@page import="entity.LocationHistory" %>
<%@page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>와이파이 정보 구하기</title>
	<style type="text/css">
		table{
			width: 100%;
			margin-top: 20px;
			border-collapse : collapse;
			border : 1px solid #ddd;
		}
		th, td{
			border : 1px solid #ddd;
		}
		th{
			height: 40px;
			background-color:#04AA6D;
			color:white;
		}
		td{
			height: 35px;
			padding : 0px 10px 0px 10px;
		}
		tr:nth-child(even){
			background-color: #f2f2f2;
		}
		tr:hover {
			background-color: #b4e3d2;
		}
		
		.btnTd{
			text-align: center;
		}
		.btn{
		    padding: 0px 10px 0px 10px;
		    border: 1px solid #e7e7e7;
		    border-radius: 3px;
			background-color: #e7e7e7; 
			color: black;
			text-align: center;
			text-decoration: none;
		}
	</style>
</head>
<body>
	<H1>위치 히스토리 목록</H1>
	<p>
		<a href="index.jsp">홈</a>
		|
		<a href="history.jsp">위치 히스토리 목록</a>
		|
		<a href="load-wifi.jsp">Open Api 와이파이 정보 가져오기</a>
	</p>
	<table>
		<thead>
			<tr>
				<th>ID</th>
				<th>X좌표</th>
				<th>Y좌표</th>
				<th>조회일자</th>
				<th>비고</th>
			</tr>
		</thead>
		<tbody>
			<%
				ApiController api = new ApiController();
				List<LocationHistory> list = api.getHistory();
				
				for(LocationHistory r : list){
			%>
			<tr>
				<td><%= r.getId() %></td>
				<td><%= r.getLhLat() %></td>
				<td><%= r.getLhLnt() %></td>
				<td><%= r.getViewDate() %></td>
				<td class="btnTd">
					<a class="btn" href="history-del.jsp?id=<%= r.getId() %>">삭제</a>
				</td>
			</tr>
			<%
				}
			%>
		</tbody>
	</table>
</body>
</html>