package com.cobitsa.jarvis.com.cobitsa.jarvis.bus.ride;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class setRideBus {
    public static String data = null;
    static Context context;

    public setRideBus(Context context) {
        this.context = context;
   }

    public static String isThereBus(String stId, String busNumber) {
        String key ="zwJNxpX4qlMxyfwRKhLbvSuV000Vr2BEL6rIbAYOx08etlxctdNp8pPyXUfMZ7I7oa9H3JHbfDArkQsu2pNycw==";
        String queryUrl = "http://ws.bus.go.kr/api/rest/stationinfo/getRouteByStation?ServiceKey="+ key +"&arsId=" + stId;

        StringBuffer buffer=new StringBuffer();

        try {
            URL url = new URL(queryUrl);

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            InputStream is = new ByteArrayInputStream(busNumber.getBytes());

            parser.setInput(is, "UTF-8");

            String tag;

            parser.next();

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        tag= parser.getName();
                        if (tag.equals("busRouteId")) {
                            parser.next();
                            buffer.append(parser.getText());
                            data = buffer.toString();
                            return data;
                        }
                        break;
                    case XmlPullParser.TEXT:
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

        return data;
    }
    //나중에 trace 버스랑 연결
}
