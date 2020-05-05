package com.cobitsa.jarvis.com.cobitsa.jarvis.bus.ride;

import android.content.Context;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class setRideBus {
    private static String data = null;
    static Context context;

    public setRideBus(Context context) {
        this.context = context;
    }

    public static void isThereBus(String stId, String busNumber) {
        String key ="인증키";

        String queryUrl = "http://ws.bus.go.kr/api/rest/stationinfo/getRouteByStation?ServiceKey="+ key +"&arsId=" + stId;
        boolean inbusRouteNm = false;
        String busRouteNm = null;

        try {
            URL url = new URL(queryUrl);

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("busRouteNm")) {
                            inbusRouteNm = true;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if(inbusRouteNm){
                            busRouteNm = parser.getText();
                            inbusRouteNm = false;
                            if(busRouteNm == busNumber)
                                data = busRouteNm;
                        }
                        break;
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (data==null){
            Toast.makeText(context,"위 정류장에는 해당 버스 노선이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    //만약 Background 함수 작성 완료시 호출
}
