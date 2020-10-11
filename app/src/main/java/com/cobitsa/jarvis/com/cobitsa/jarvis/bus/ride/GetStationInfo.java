package com.cobitsa.jarvis.com.cobitsa.jarvis.bus.ride;

import android.app.Activity;

import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common.GpsTracker;
import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common.ParsingXML;

import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import static com.cobitsa.jarvis.MainActivity.userData;

public class GetStationInfo {

    String key;             // 인증키
    private final static int START_DISTANCE_FOR_GET_STATION = 30;

    // 인증키 값으로 초기화
    public GetStationInfo(String key) {
        this.key = key;
    }


    public Boolean checkWhereAmI(Activity mainActivity) {
        GpsTracker gpsTracker = new GpsTracker(mainActivity);
        double latitute = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();
        ArrayList<String> stInfo = this.getApiData(Double.toString(longitude),Double.toString(latitute),  Integer.toString(START_DISTANCE_FOR_GET_STATION));


        if (stInfo == null)
            return false;
        userData.setStartStation(stInfo.get(0), stInfo.get(1), stInfo.get(2));
        return true;
    }

    // 현재 위치를 기반으로 제일 가까운 정류소의 정보 리턴
    // @param tmX : GPS X 좌표
    // @param tmY : GPS Y 좌표
    // @param radius : 반경(0~1500m)
    // @return List(0) : 정류소 아이디
    // @return List(1) : 정류소 이름
    // @return List(2) : 정류소 고유번호
    public ArrayList<String> getApiData(String tmX, String tmY, String radius) {
        String url = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos" +
                "?ServiceKey=" + key +
                "&tmX=" + tmX +
                "&tmY=" + tmY +
                "&radius=" + radius;
        ArrayList<String> stInfo = new ArrayList<>();
        String stId = "";
        String stName = "";
        String arsId = "";

        try {
            ParsingXML parsingXML = new ParsingXML(url);
            if (parsingXML.getLength() == 1) {
                stId = parsingXML.parsing("stationId", 0);
                stName = parsingXML.parsing("stationNm", 0);
                arsId = parsingXML.parsing("arsId", 0);
            } else if (parsingXML.getLength() == 0)
                return null;
            else {
                int nextRadius = Integer.parseInt(radius) / 2;
                stInfo = getApiData(tmX, tmY, Integer.toString(nextRadius));
                return stInfo;
            }
        } catch (ParserConfigurationException | InterruptedException e) {
            e.printStackTrace();
        }

        stInfo.add(stId);
        stInfo.add(stName);
        stInfo.add(arsId);

        return stInfo;
    }
}
