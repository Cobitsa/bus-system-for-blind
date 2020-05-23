package com.cobitsa.jarvis.com.cobitsa.jarvis.bus.ride;

import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common.ParsingXML;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

public class GetStationInfo {

    String key;             // 인증키

    // 인증키 값으로 초기화
    public GetStationInfo(String key){
        this.key = key;
    }

    // 현재 위치를 기반으로 제일 가까운 정류소의 정보 리턴
    // @param tmX : GPS X 좌표
    // @param tmY : GPS Y 좌표
    // @param radius : 반경(0~1500m)
    // @return List(0) : 정류소 아이디
    // @return List(1) : 정류소 이름
    // @return List(2) : 정류소 고유번호
    public ArrayList<String> get(String tmX, String tmY, String radius){
        String url = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos" +
                            "?ServiceKey=" + key +
                            "&tmX=" + tmX +
                            "&tmY=" + tmY +
                            "&radius=" + radius;
        ArrayList<String> stInfo = new ArrayList<>();
        String stId = "";
        String stName = "";
        String arsId = "";

        try{
            ParsingXML parsingXML = new ParsingXML(url);
            if(parsingXML.getLength() == 1) {
                stId = parsingXML.parsing("stationId", 0);
                stName = parsingXML.parsing("stationNm", 0);
                arsId = parsingXML.parsing("arsId", 0);
            }
            else{
                int nextRadius = Integer.parseInt(radius)/2;
                stInfo = get(tmX, tmY, Integer.toString(nextRadius));
                return stInfo;
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        stInfo.add(stId);
        stInfo.add(stName);
        stInfo.add(arsId);

        return stInfo;
    }
}
