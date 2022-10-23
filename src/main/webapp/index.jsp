<%@page import="api.ApiController"%>
<%@page import="entity.OpenwifiInfo" %>
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
			text-align: center;
		}
		th{
			height: 40px;
			background-color:#04AA6D;
			color:white;
		}
		td{
			height: 50px;
			padding : 0px 10px 0px 10px;
		}
		tr:nth-child(even){
			background-color: #f2f2f2;
		}
		tr:hover {
			background-color: #b4e3d2;
		}
	</style>
</head>
<body>
	<H1>와이파이 정보 구하기</H1>
	<p>
		<a href="index.jsp">홈</a>
		|
		<a href="history.jsp">위치 히스토리 목록</a>
		|
		<a href="load-wifi.jsp">Open Api 와이파이 정보 가져오기</a>
	</p>
	<%
		String x = request.getParameter("myLat");
		String y = request.getParameter("myLnt");
	%>
	<form action="index.jsp" method="get" id="myform">
		LAT :
		<input type="text" name="myLat" id="myLat" value="<%= x != null? x : ""%>"/>
		, LNT :
		<input type="text" name=myLnt id="myLnt" value="<%= y != null? y : ""%>"/>
		<button type="button" id="myLocationBtn">내 위치 가져오기</button>
		<button type="submit" id="locationBtn">근처 WIFI 정보 보기</button>
	</form>
	<table>
		<thead>
			<tr>
				<th>거리(Km)</th>
				<th>관리번호</th>
				<th>자치구</th>
				<th>와이파이명</th>
				<th>도로명주소</th>
				<th>상세주소</th>
				<th>설치위치(층)</th>
				<th>설치유형</th>
				<th>설치기관</th>
				<th>서비스구분</th>
				<th>망종류</th>
				<th>설치년도</th>
				<th>실내외구분</th>
				<th>WIFI접속환경</th>
				<th>X좌표</th>
				<th>Y좌표</th>
				<th>작업일자</th>
			</tr>
		</thead>
		<tbody>
		<%
			System.out.printf("jsp >> x는 %s, y는 %s \n", x, y);
			System.out.println("==========================");
			if(x != null && y != null && !x.isEmpty() && !y.isEmpty()){
				ApiController api = new ApiController();
				List<OpenwifiInfo> list = api.nearApi(x, y);
				
				for(OpenwifiInfo r : list){
		%>
			<tr>
				<td><%=r.getDistance() %></td>
				<td><%=r.getMgrNo() %></td>
				<td><%=r.getMrdofc() %></td>
				<td><%=r.getMainNm() %></td>
				<td><%=r.getAdres1() %></td>
				<td><%=r.getAdres2() %></td>
				<td><%=r.getInstlFloor() %></td>
				<td><%=r.getInstlTy() %></td>
				<td><%=r.getInstlMby() %></td>
				<td><%=r.getSvcSe() %></td>
				<td><%=r.getCmcwr() %></td>
				<td><%=r.getCnstcYear() %></td>
				<td><%=r.getInoutDoor() %></td>
				<td><%=r.getRemars3() %></td>
				<td><%=r.getLat() %></td>
				<td><%=r.getLnt() %></td>
				<td><%=r.getWorkDttm() %></td>
			</tr>
		<%
				}
			}else{
		%>
			<tr>
				<td colspan="17">위치 정보를 입력한 후에 조회해 주세요.</td>
			</tr>

		<%		
			}
			
		%>
		</tbody>
	</table>
	<script type="text/javascript">
		var myLocation = document.getElementById('myLocationBtn');
		var myLat = document.getElementById('myLat');
		var myLnt = document.getElementById('myLnt');
	 	var latitude = ''; 	
	 	var longitude = '';
	 	
	 	//내 위치 가져오기
	    function success({ coords }) {
	        latitude = coords.latitude;   // 위도
	        longitude = coords.longitude; // 경도
	    }
	
	    function getUserLocation() {
	        if (!navigator.geolocation) {
	            throw "위치 정보가 지원되지 않습니다.";
	        }
	        navigator.geolocation.getCurrentPosition(success);
	    }
	
   		getUserLocation();
		
	    myLocation.addEventListener('click', event => {
	    	myLat.value = latitude;
	    	myLnt.value = longitude;
		});
	    
	</script>
</body>
</html>