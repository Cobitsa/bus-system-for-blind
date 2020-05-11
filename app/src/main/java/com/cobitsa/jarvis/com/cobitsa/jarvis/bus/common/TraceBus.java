package com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class TraceBus {

    String key;         // 서비스 키
    boolean isArrived;  // 이전 정류장 도착시 루프 브레이커

    // TraceBus 생성자
    // @param 서비스 키 값
    public TraceBus(String key) {
        this.key = key;
        isArrived = false;
    }

    // API Url 생성후 스케쥴러 메소드로 넘겨주는 함수
    // @param 정류소 ID, 차량 ID, 탑승예정 or 탑승 중 플레그
    // @flag = 1 탑승 예정 버스인 경우
    // @flag = 2 탑승 중인 버스인 경우
    public void tracing(String stId, String vehId, int flag) {
        String queryUrl = "http://ws.bus.go.kr/api/rest/buspos/getBusPosByVehId" +
                "?ServiceKey=" + key +
                "&vehId=" + vehId;

        checkBusLoc(queryUrl, stId, flag);
    }

    // 스케쥴러 메소드, 10초마다 버스 위치를 확인한다.
    // 이전 정류장에 도착하면 플레그에 따라 다음 메소드를 호출
    // @param API URL, 정류소 ID, 탑승예정 or 탑승 중 플레그
    public void checkBusLoc(final String queryUrl, final String stId, final int flag) {
        final Timer timer = new Timer();
        final TimerTask task = new TimerTask() {
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
                                    isArrived = true;
                                }
                                break;
                            case XmlPullParser.TEXT:
                                if(isArrived) {
                                    if(parser.getText() == stId) {

                                        switch(flag) {
                                            case 1:
                                                // 탑승 예정 버스가 이전 정류장 도착한 경우
                                                // 사용자에게 TTS로 정보알림
                                                // 버스기사 단말기에 탑승자 있음 정보알림
                                                break;
                                            case 2:
                                                // 탑승 중인 버스가 이전 정류장 도착한 경우
                                                // 사용자에게 TTS로 정보알림
                                                break;
                                        }
                                        timer.cancel();
                                    }
                                }
                                break;
                        }
                    }
                } catch (MalformedURLException | XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        // 10초 주기로 스케줄러 실행
        timer.schedule(task, 0, 10000);
    }
}
