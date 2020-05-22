package com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

public class GetPrevStId {

    String key;

    public GetPrevStId(String key){
        this.key = key;
    }

    public  String get(String busRouteId, String stId){
        String url = "http://ws.bus.go.kr/api/rest/busRouteInfo/getStaionByRoute" +
                             "?ServiceKey=" + key +
                             "&busRouteId=" + busRouteId;
        String prevStId = "";
        int preIndex;

        try{
            ParsingXML parsingXML = new ParsingXML(url);
            preIndex = parsingXML.index("station", stId) - 1;
            prevStId = parsingXML.parsing("station", preIndex);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return prevStId;
    }
}
