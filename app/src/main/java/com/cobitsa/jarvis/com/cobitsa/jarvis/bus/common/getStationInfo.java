package com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common;

import android.content.Context;

import com.cobitsa.jarvis.com.cobitsa.jarvis.voice.TTS;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class getStationInfo {

    static Context context;

    public getStationInfo() {
        this.context = context;
    }

    String key ="zwJNxpX4qlMxyfwRKhLbvSuV000Vr2BEL6rIbAYOx08etlxctdNp8pPyXUfMZ7I7oa9H3JHbfDArkQsu2pNycw==";
    private TTS tts;

    int count = 0;
    int radius = 10;
    String data;
    String stopID;
    String stopName;
    String stopAddr;
    String result;


    public String callAPI(double xLocation, double yLocation) {
        String queryUrl = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos?ServiceKey="+ key +"&tmX="+ xLocation +"&tmY="+ yLocation +"&radius="+ radius;

        StringBuffer buffer=new StringBuffer();

        try {
            URL url = new URL(queryUrl);

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), "UTF-8");

            String tag;

            parser.next();

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        tag= parser.getName();
                        if (tag.equals("arsId")) {
                            parser.next();
                            buffer.append(parser.getText());
                            data = buffer.toString();
                            count++;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if (count > 1)
                        {
                            radius = radius -1;
                            callAPI(xLocation,yLocation);
                        }
                        break;
                }
                result = findData(data);
            }
        } catch (MalformedURLException | XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public  String findData(String data){
        String queryUrl = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid?ServiceKey="+ key +"&arsId="+ data;
        boolean instId= false, instNm = false, inadirection = false;
        String stId = null, stNm = null, adirection = null;
        tts = new TTS();
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
                            instId = true;
                        }
                        if (parser.getName().equals("stNm")) {
                            instNm = true;
                        }
                        if (parser.getName().equals("adirection")) {
                            inadirection = true;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if(instId){
                            stId = parser.getText();
                            instId = false;
                            stopID = stId;
                        }
                        if(instNm){
                            stNm = parser.getText();
                            instNm = false;
                            stopName = stNm;
                        }
                        if(inadirection){
                            adirection = parser.getText();
                            inadirection = false;
                            stopAddr = adirection;
                        }
                        break;
                }
            }
            String message = "정류장의 이름은 " + stopName + "이며" + stopAddr + "방면입니다. 정류장 고유번호는 "+ data +"입니다.";
            tts.speech(message);

        } catch (MalformedURLException | XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stopID;
    }

}

