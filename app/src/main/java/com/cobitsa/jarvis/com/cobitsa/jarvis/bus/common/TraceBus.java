package com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class TraceBus {

    String key ="인증키";

    String stID;        // 정류소 ID        - 내부에 저장되어 있는 현재 정류장 정보
    String stOrd;       // 정류소 순번       - 내부에 저장되어 있는 현재 정류장 순번
    String preStID;     // 이전 정류소 ID    - 내부에 저장되어 있는 이전 정류장 정보
    String preStOrd;    // 이전 정류소 순번   - 내부에 저장되어 있는 이전 정류장 순번

    public boolean traceBus(String busRouteId) {

        boolean isArrived = false;      // 리턴값

        // 버스 도착정보 2번 API   getArrInfoByRouteList 사용
        String queryUrl = "http://ws.bus.go.kr/api/rest/arrive/getArrInfoByRoute" +
                "?ServiceKey=" + key +
                "&stId=" + preStID +
                "&busRouteId=" + busRouteId +
                "&ord=" + preStOrd;

        boolean breaker = false;

        try {
            URL url = new URL(queryUrl);

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("isArrive1")) {
                            breaker = true;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if(breaker) {
                            breaker = false;
                            if(parser.getText() == "1") {       // API 반환값 (0:운행중, 1:도착)
                                isArrived = true;
                            } // API출력값이 LIST가 아니기 때문에 여기서 바로 while문 중지하고 리턴해도 될듯.
                        }
                        break;
                }
            }
        } catch (MalformedURLException | XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        return isArrived;
    }
}
