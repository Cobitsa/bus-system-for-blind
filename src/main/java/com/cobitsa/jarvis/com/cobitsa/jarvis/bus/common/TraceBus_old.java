package com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class TraceBus_old {

    String key ="인증키";

    String stId;        // 정류소 ID        - 내부에 저장되어 있는 현재 정류장 정보
    String vehId;       // 버스 ID          - 탑승 예정 또는 중인 버스 정보

    boolean isArrived = false;      // 리턴값
    boolean breaker = false;

    Timer timer = new Timer();

    public boolean traceBus(String busRouteId) {

        // 버스 도착정보 2번 API   getBusPosByVehIdItem 사용
        final String queryUrl = "http://ws.bus.go.kr/api/rest/buspos/getBusPosByVehId&vehId=111033115" +
                "?ServiceKey=" + key +
                "&vehId=111033115" + vehId;

        // 타이머 선언
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                try {
                    URL url = new URL(queryUrl);

                    XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = parserCreator.newPullParser();

                    parser.setInput(url.openStream(), null);

                    int parserEvent = parser.getEventType();

                    while (parserEvent != XmlPullParser.END_DOCUMENT) {
                        switch (parserEvent) {
                            case XmlPullParser.START_TAG:
                                if (parser.getName().equals("stId")) {
                                    breaker = true;
                                }
                                break;
                            case XmlPullParser.TEXT:
                                if(breaker) {
                                    breaker = false;
                                    if(parser.getText() == stId) {       // API 반환값 (0:운행중, 1:도착)
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

                if(isArrived == true) {
                    timer.cancel();
                }
            }
        };
        timer.schedule(task, 1000, 10000);      // 1초 뒤부터 10초마다 실행

        return isArrived;
    }
}
