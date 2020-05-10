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

    String key;
    String stId;
    String busRouteId;
    String vehId;
    String queryUrl;

    boolean isArrived;
    boolean bloop;

    int count;

    public TraceBus(String key, String stId, String busRouteId, String vehId) {
        this.key = key;
        this.stId = stId;
        this.busRouteId = busRouteId;
        this.vehId = vehId;
        queryUrl = "http://ws.bus.go.kr/api/rest/buspos/getBusPosByVehId" +
                "?ServiceKey=" + key +
                "&vehId=" + vehId;
        isArrived = false;
        count = 0;

    }

    public boolean tracing() {

        checkBusLoc();
        while(true) {
            if(isArrived)
                break;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return isArrived;
    }

    public void checkBusLoc() {

        final Timer timer = new Timer();

        final TimerTask task = new TimerTask() {
            @Override
            public void run() {

                if(count == 5) {
                    isArrived = true;
                    timer.cancel();
                }

                count++;

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
                                    bloop = true;
                                }
                                break;
                            case XmlPullParser.TEXT:
                                if(bloop) {
                                    bloop = false;
                                    if(parser.getText() == stId) {
                                        isArrived = true;
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
        timer.schedule(task, 0, 1000);
    }

}
