package com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import com.cobitsa.jarvis.com.cobitsa.jarvis.voice.TTS;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class findBusStop {

    String key ="인증키";
    private TTS tts;

    int radius;
    String data;
    String stopID;
    String stopName;
    String stopAddr;


    public String callAPI(double xLocation, double yLocation) {
        String queryUrl = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos?ServiceKey="+ key +"&tmX="+ xLocation +"&tmY="+ yLocation +"&radius="+ radius;

        //tts = new TTS(getApplicationContext());
        boolean inarsId = false;
        String arsId = null;
        try {
            URL url = new URL(queryUrl);

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("arsId")) {
                            inarsId = true;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if(inarsId){
                            arsId = parser.getText();
                            inarsId = false;
                            data = arsId;
                            findData(data);
                        }
                        break;
                }
            }
        } catch (MalformedURLException | XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
    public  void findData(String data){
        String queryUrl = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid?ServiceKey="+ key +"&arsId="+ data;
        boolean instId= false, instNm = false, inadirection = false;
        String stId = null, stNm = null, adirection = null;

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
        } catch (MalformedURLException | XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    String message = "정류장의 이름은 " + stopName + "이며" + stopAddr + "방면입니다. 정류장 고유번호는 "+ stopID +"입니다.";
            //TTS 호출 완성되면 stopID, stopName, stopAddr 인자로 호출
    //tts.speech(message);
}

