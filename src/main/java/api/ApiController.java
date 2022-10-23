package api;

import java.util.List;

import entity.LocationHistory;
import entity.OpenwifiInfo;

public class ApiController {
	ApiService apiService = new ApiService();
	
	//위치 히스토리 삭제
	public void delHistory(String id){
		apiService.historyDelete(id);
	}
	
	//위치 히스토리 불러오기
	public List<LocationHistory> getHistory(){
		List<LocationHistory> hsList = apiService.historyList();

		return hsList;
	}
	
	//근처 WIFI 정보 불러오기
	public List<OpenwifiInfo> nearApi(String x, String y) {
		System.out.println("nearApi 호출");
		//입력받은 좌표 저장(히스토리)
		apiService.gpsHistory(x, y);
		//입력받은 좌표와 db 좌표 거리 계산
		List<OpenwifiInfo> wifiList = apiService.apiDistance(x,y);
		
		return wifiList;
	}
	
	//WIFI 정보 불러오기
	public String getApi() {
	    
		String response1 = apiService.run("http://openapi.seoul.go.kr:8088/6a546773746b656a333772616d4473/json/TbPublicWifiInfo/1/1/");
    	int endidx = Integer.parseInt(response1) / 1000 + 1; //18
    	System.out.println("endidx>>"+endidx);
	  
    	int start = 1;
    	int end = start * 1000;
	    for(int i = 0; i < endidx; i++) {
	    	apiService.apiCreate("http://openapi.seoul.go.kr:8088/6a546773746b656a333772616d4473/json/TbPublicWifiInfo/"+start+"/"+end+"/");
	    	start = end + 1;
	    	end = end + 1000;
	    }
	    
	    return response1;
	}
}
