package com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.cobitsa.jarvis.com.cobitsa.jarvis.voice.TTS;

import javax.xml.parsers.ParserConfigurationException;

public class TraceBus {

    String key;         // 서비스 키
    String url;

    ParsingXML parsingXML;

    // TraceBus 생성자
    // @param 서비스 키 값
    public TraceBus(String key) {
        this.key = key;
    }

    // API Url 생성후 스케쥴러 메소드로 넘겨주는 함수
    // @param 정류소 ID, 차량 ID, 탑승예정 or 탑승 중 플레그
    // @flag = 1 탑승 예정 버스인 경우
    // @flag = 2 탑승 중인 버스인 경우
    public void tracing(String prevStId, String vehId, int flag) {

        url = "http://ws.bus.go.kr/api/rest/buspos/getBusPosByVehId" +
                "?ServiceKey=" + key +
                "&vehId=" + vehId;

        checkBusLoc(url, prevStId, flag);


    }

    // 스케쥴러 메소드, 10초마다 버스 위치를 확인한다.
    // 이전 정류장에 도착하면 플레그에 따라 다음 메소드를 호출
    // @param API URL, 정류소 ID, 탑승예정 or 탑승 중 플레그
    public void checkBusLoc(final String url, final String stId, final int flag) {


        final TTS tts = new TTS();
        final Timer timer = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    System.out.println("스케줄러 실행!!");
                    ParsingXML parsingXML = new ParsingXML(url);
                    String s = "";
                    s = parsingXML.parsing("stId", 0);
                    if (s.equals(stId)) {
                        //탑승 예정 버스가 이전 정류장 도착한 경우
                        if (flag == 1) {
                            tts.speech("탑승 예정 버스가 이전 정류장을 출발했습니다. 탑승준비를 해주세요.");
                            // 버스기사 단말기에 탑승자 있음 정보알림
                        }
                        // 탑승 중인 버스가 이전 정류장 도착한 경우
                        else if (flag == 2) {
                            tts.speech("목적지가 다음 정류장 입니다. 하차준비를 해주세요.");
                        }

                        timer.cancel();
                    }
                } catch (ParserConfigurationException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        // 10초 주기로 스케줄러 실행
        timer.schedule(task, 0, 10000);
    }

    private String getPreStId(String url, String stId) {
        String preStId = "";
        int preIndex = -1;
        try {
            parsingXML = new ParsingXML(url);
            preIndex = parsingXML.index("station", stId) - 1;
            preStId = parsingXML.parsing("station", preIndex);
        } catch (ParserConfigurationException | InterruptedException e) {
            e.printStackTrace();
        }

        return preStId;
    }
}
