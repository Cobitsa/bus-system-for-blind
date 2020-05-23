package com.cobitsa.jarvis.com.cobitsa.jarvis.bus.ride;

import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common.ParsingXML;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

public class SetRideBus {

    String key;                 // 서비스 키

    // 키값으로 초기화
    public SetRideBus(String key){
        this.key = key;
    }

    // 탑승할 버스 정보를 List로 반환하는 메소드
    // @param arsId : 현재 정류소의 고유번호
    // @param sttBus : STT로 받은 탑승하려는 버스 번호
    // @return List(0) : 버스 번호
    // @return List(1) : 버스 노선 아이디
    public ArrayList<String> set(String arsId, String sttBus){

        String url = "http://ws.bus.go.kr/api/rest/stationinfo/getRouteByStation" +
                        "?ServiceKey=" + key +
                        "&arsId=" + arsId;

        ArrayList<String> infoList = new ArrayList<>();
        String tmpNum = "";
        String tmpRouteId = "";

        try{
            ParsingXML parsingXML = new ParsingXML(url);

            for(int i = 0; i < parsingXML.getLength(); i++){
                if(parsingXML.parsing("busRouteNm", i).equals(sttBus)){
                    tmpNum = parsingXML.parsing("busRouteNm", i);
                    tmpRouteId = parsingXML.parsing("busRouteId", i);
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        infoList.add(tmpNum);
        infoList.add(tmpRouteId);

        return  infoList;
    }

    // 탑승하려는 노선의 버스 중 첫번째로 도착예정인 버스의 차량 아이디 반환
    // @param stId : 현재 정류소의 아이디
    // @param busRouteId : 탑승하려는 버스 노선 아이디
    // @return 첫번째 도착예정인 차량 아이디
    public String setVehId(String stId, String busRouteId){

        String url = "http://ws.bus.go.kr/api/rest/arrive/getArrInfoByRouteAll" +
                            "?ServiceKey=" + key +
                            "&busRouteId=" + busRouteId;
        String vehId = "";

        try{
            ParsingXML parsingXML = new ParsingXML(url);
            for(int i = 0; i < parsingXML.getLength(); i++){
                if(parsingXML.parsing("stId", i).equals(stId)){
                    vehId = parsingXML.parsing("vehId1", i);
                    break;
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return vehId;
    }
}
