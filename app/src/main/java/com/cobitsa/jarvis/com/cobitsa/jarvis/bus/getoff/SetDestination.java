package com.cobitsa.jarvis.com.cobitsa.jarvis.bus.getoff;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

public class SetDestination {

    String key;                         // 서비스 키
    HashMap<String, String> stList;     // 생성할 노선에 대한 정류소 리스트 <이름, ID>

    // SetDestination 생성자
    // @param key = 서비스 키
    public SetDestination(String key) {
        this.key = key;
        stList = new HashMap<>();
    }

    // 메인 메소드
    // @param 버스 노선 ID, STT로 받은 정류소 명
    // @return 유효한 정류소인 경우 정류소 ID
    // @return 유효하지 않은 정류소인 경우 ""
    public String setDestination(String busRouteId, String sttDestination) {

        String destinationId = "";      // return 값

        // 정류소 리스트 생성 (HashMap)
        stList = makeStList(busRouteId);

        // 유효한 정류소인지 확인
        // 유효하다면 리턴값 변경
        if(findSt(sttDestination, stList)) {
            destinationId = stList.get(sttDestination);
        }

        return destinationId;
    }

    // 정류소 리스트를 생성하는 메소드
    // @param 버스 노선 ID
    // @return 생성된 정류소 리스트 (HashMap)
    public HashMap<String, String> makeStList(String busRouteId) {

        HashMap<String, String> hashMap = new HashMap<>();
        boolean breakbyName = false;    // 루프 제어 1
        boolean breakbyId = false;      // 루프 제어 2
        int breakloop = 0;              // 루프 제어 3
        String tmpName = "";            // 임시 변수 1
        String tmpId = "";              // 임시 변수 2

        // API URL 생성
        final String queryUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getStaionByRoute" +
                "?ServiceKey=" + key +
                "&busRouteId=" + busRouteId;

        try {
            URL url = new URL(queryUrl);

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        // 정류소 이름 찾으면
                        if (parser.getName().equals("stationNm")) {
                            breakbyName = true;
                        }
                        // 정류소 아이디 찾으면
                        if (parser.getName().equals("arsId")) {
                            breakbyId = true;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        // 임시 변수에 이름 저장
                        if(breakbyName) {
                            tmpName = parser.getName();
                            breakbyName = false;
                            breakloop++;
                        }
                        // 임시 변수에 아이디 저장
                        if(breakbyId) {
                            tmpId = parser.getName();
                            breakbyId = false;
                            breakloop++;
                        }
                        // 이름과 아이디를 모두 찾은 상황이면 리스트에 저장
                        if(breakloop == 2) {
                            hashMap.put(tmpName, tmpId);
                            breakloop = 0;
                            tmpName = "";
                            tmpId = "";
                        }
                        break;
                }
            }
        } catch (MalformedURLException | XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hashMap;
    }

    // STT로 입력받은 정류소 이름이 유효한 정유장인지 확인
    // @param STT로 입력받은 정류소, 정류소 리스트
    // @return 유효하다면 true, 유효하지 않다면 false
    public boolean findSt(String sttDestination, HashMap<String, String> hashMap) {

        boolean isExist = false;
        Set<String> keys = hashMap.keySet();

        for (String key : keys) {
            if (key.equals(sttDestination)) {
                isExist = true;
            }
        }

        return isExist;
    }
}
