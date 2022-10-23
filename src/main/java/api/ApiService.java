package api;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import entity.LocationHistory;
import entity.OpenwifiInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiService {
	final OkHttpClient client = new OkHttpClient();

	String dbFile = "jdbc:sqlite:C:\\dev\\sqlite-tools-win32-x86-3390400\\openwifi_db.db";
	Connection con = null;
	PreparedStatement prest = null;
	ResultSet rs = null;
	
	//히스토리 삭제
	public void historyDelete(String id){
		List<LocationHistory> hsList = new ArrayList<>();
		
		try{
			//jdbc 연결
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection(dbFile);
			System.out.println("연결확인");
			
			String sql = "DELETE FROM location_history WHERE id=?";
			
			prest = con.prepareStatement(sql);
			prest.setString(1, id);
			
			int affected = prest.executeUpdate();
			
			if(affected > 0) {
				System.out.println("삭제 성공");
			}else {
				System.out.println("삭제 실패");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (prest != null && !prest.isClosed()) {
					prest.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//히스토리 불러오기
	public List<LocationHistory> historyList(){
		List<LocationHistory> hsList = new ArrayList<>();
		
		try{
			//jdbc 연결
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection(dbFile);
			System.out.println("연결확인");
			
			String sql = "SELECT id, lat, lng, view_date from location_history";
			
			prest = con.prepareStatement(sql);
			
			rs = prest.executeQuery();
			
			LocationHistory hs = null;
			while (rs.next()) {
				hs = new LocationHistory();
				hs.setId(rs.getInt("id"));
				hs.setLhLat(rs.getString("lat"));
				hs.setLhLnt(rs.getString("lng"));
				hs.setViewDate(rs.getString("view_date"));

				hsList.add(hs);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
                if(rs != null && !rs.isClosed()){
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
			try {
				if (prest != null && !prest.isClosed()) {
					prest.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return hsList;
	}
	
	
	//입력받은 좌표 저장(히스토리)
	public void gpsHistory(String x, String y){
		OpenwifiInfo wifi = new OpenwifiInfo();
		System.out.printf("입력받은 좌표 저장 -- x는 %s, y는 %s \n", x, y);
		
		try{
			//jdbc 연결
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection(dbFile);
			System.out.println("연결확인");
			
			String sql = "INSERT INTO location_history"
					+ " (lat, lng, view_date)"
					+ " VALUES"
					+ " (?,?,?);";
			
			prest = con.prepareStatement(sql);
			prest.setString(1, x);
			prest.setString(2, y);
			prest.setString(3, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
			
			int affected = prest.executeUpdate();
			if(affected > 0){
                System.out.println("저장 성공");
            }else {
                System.out.println("저장 실패");
            }
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (prest != null && !prest.isClosed()) {
					prest.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//입력받은 좌표와 db 좌표 거리 계산
	public List<OpenwifiInfo> apiDistance(String x, String y){
		List<OpenwifiInfo> wifiList = new ArrayList<>();
		System.out.printf("입력받은 좌표와 db 좌표 거리 계산 -- x는 %s, y는 %s \n", x, y);
		
		try{
			//jdbc 연결
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection(dbFile);
			System.out.println("연결확인");
			
			String sql = "SELECT round((ABS(? - A.lat) + ABS(? - A.lnt)) * 100, 4) as distance, *"
					+ " FROM openwifi_info as A"
					+ " ORDER BY distance ASC LIMIT 20";
			
			prest = con.prepareStatement(sql);
			prest.setString(1, x);
			prest.setString(2, y);
			
			rs = prest.executeQuery();
			
			OpenwifiInfo wifi = null;
			while (rs.next()){
				wifi = new OpenwifiInfo();
                wifi.setDistance(rs.getString("distance"));
                wifi.setMgrNo(rs.getString("mgr_no"));
				wifi.setMrdofc(rs.getString("mrdofc"));
				wifi.setMainNm(rs.getString("main_nm"));
				wifi.setAdres1(rs.getString("adres1"));
				wifi.setAdres2(rs.getString("adres2"));
				wifi.setInstlFloor(rs.getString("instl_floor"));
				wifi.setInstlTy(rs.getString("instl_ty"));
				wifi.setInstlMby(rs.getString("instl_mby"));
				wifi.setSvcSe(rs.getString("svc_se"));
				wifi.setCmcwr(rs.getString("cmcwr"));
				wifi.setCnstcYear(rs.getString("cnstc_year"));
				wifi.setInoutDoor(rs.getString("inout_door"));
				wifi.setRemars3(rs.getString("remars3"));
				wifi.setLat(rs.getString("lat"));
				wifi.setLnt(rs.getString("lnt"));
				wifi.setWorkDttm(rs.getString("work_dttm"));
                
                wifiList.add(wifi);
				
            }
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
                if(rs != null && !rs.isClosed()){
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
			try {
				if (prest != null && !prest.isClosed()) {
					prest.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return wifiList;
	}
	
	//api 데이터 db 저장
	public String apiCreate(String url){
		Request request = new Request.Builder().url(url).build();
		String json = null;
		OpenwifiInfo wifi = new OpenwifiInfo();
		String message ="";
		
		try{
			//api - josn 변환
			Response response = client.newCall(request).execute();
			json = response.body().string();

			JsonElement element = JsonParser.parseString(json);

			JsonObject tbPublicWifiInfoObject = element.getAsJsonObject().get("TbPublicWifiInfo").getAsJsonObject();

			JsonArray rowArray = tbPublicWifiInfoObject.get("row").getAsJsonArray();
			json = tbPublicWifiInfoObject.get("list_total_count").getAsString();
			
			//jdbc 연결
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection(dbFile);
			//System.out.println("연결확인");
			
			con.setAutoCommit(false);
			
			String sql = "INSERT INTO openwifi_info"
					+ " (mgr_no, mrdofc, main_nm, adres1, adres2, instl_floor, instl_mby, instl_ty, svc_se, cmcwr, cnstc_year, inout_door, remars3, lat, lnt, work_dttm)"
					+ " VALUES"
					+ " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			prest = con.prepareStatement(sql);
			
			for (int i = 0; i < rowArray.size(); i++) {
				JsonObject row = rowArray.get(i).getAsJsonObject();
				
				wifi.setMgrNo(row.get("X_SWIFI_MGR_NO").getAsString());
				wifi.setMrdofc(row.get("X_SWIFI_WRDOFC").getAsString());
				wifi.setMainNm(row.get("X_SWIFI_MAIN_NM").getAsString());
				wifi.setAdres1(row.get("X_SWIFI_ADRES1").getAsString());
				wifi.setAdres2(row.get("X_SWIFI_ADRES2").getAsString());
				wifi.setInstlFloor(row.get("X_SWIFI_INSTL_FLOOR").getAsString());
				wifi.setInstlTy(row.get("X_SWIFI_INSTL_TY").getAsString());
				wifi.setInstlMby(row.get("X_SWIFI_INSTL_MBY").getAsString());
				wifi.setSvcSe(row.get("X_SWIFI_SVC_SE").getAsString());
				wifi.setCmcwr(row.get("X_SWIFI_CMCWR").getAsString());
				wifi.setCnstcYear(row.get("X_SWIFI_CNSTC_YEAR").getAsString());
				wifi.setInoutDoor(row.get("X_SWIFI_INOUT_DOOR").getAsString());
				wifi.setRemars3(row.get("X_SWIFI_REMARS3").getAsString());
				wifi.setLat(row.get("LAT").getAsString());
				wifi.setLnt(row.get("LNT").getAsString());
				wifi.setWorkDttm(row.get("WORK_DTTM").getAsString());
				
				prest.setString(1, wifi.getMgrNo());
				prest.setString(2, wifi.getMrdofc());
				prest.setString(3, wifi.getMainNm());
				prest.setString(4, wifi.getAdres1());
				prest.setString(5, wifi.getAdres2());
				prest.setString(6, wifi.getInstlFloor());
				prest.setString(7, wifi.getInstlTy());
				prest.setString(8, wifi.getInstlMby());
				prest.setString(9, wifi.getSvcSe());
				prest.setString(10, wifi.getCmcwr());
				prest.setString(11, wifi.getCnstcYear());
				prest.setString(12, wifi.getInoutDoor());
				prest.setString(13, wifi.getRemars3());
				prest.setString(14, wifi.getLat());
				prest.setString(15, wifi.getLnt());
				prest.setString(16, wifi.getWorkDttm());
				
				prest.addBatch();
				prest.clearParameters();
				
				if((i % 100) == 0){
                    // 배치에 적재한 것들 실행 하여 DB에 커밋
					prest.executeBatch();
					prest.clearBatch();
                    con.commit();
                }
			}
			prest.executeBatch();
			con.commit();
			message = "U";
			
		} catch (Exception e) {
			e.printStackTrace();
			
			try {
                con.rollback();                
            }catch(Exception e1) {
                e1.printStackTrace();
            }
			message = "F";
			
		}finally {
			try {
				if (prest != null && !prest.isClosed()) {
					prest.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.printf("message >>> %s \n", message);
		return json;
	}
	
	//api 데이터 갯수 가져오기
	public String run(String url){
		Request request = new Request.Builder().url(url).build();
		String jsoncnt = null;
		OpenwifiInfo wifi = new OpenwifiInfo();
		
		try (Response response = client.newCall(request).execute()) {
			//api - josn 변환
			String json = response.body().string();

			JsonElement element = JsonParser.parseString(json);

			JsonObject tbPublicWifiInfoObject = element.getAsJsonObject().get("TbPublicWifiInfo").getAsJsonObject();

			JsonArray rowArray = tbPublicWifiInfoObject.get("row").getAsJsonArray();
			jsoncnt = tbPublicWifiInfoObject.get("list_total_count").getAsString();
			
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		return jsoncnt;
	}

}
