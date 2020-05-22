package com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ParsingXML {

    DocumentBuilderFactory dbFactoty;
    DocumentBuilder dBuilder;
    Document doc;

    public ParsingXML(String url) throws ParserConfigurationException, IOException, SAXException {
        dbFactoty = DocumentBuilderFactory.newInstance();
        dBuilder = dbFactoty.newDocumentBuilder();
        doc = dBuilder.parse(url);
    }

    // @param tag : 파싱할 태그
    // @param index : 파싱할 리스트의 인덱스
    // @return : 파싱된 문자
    public String parsing(String tag, int index) {
        String s = "";
        NodeList nodeList = doc.getElementsByTagName("itemList");

        Node node = nodeList.item(index);
        Element fstElmnt = (Element) node;

        NodeList stIdNode = fstElmnt.getElementsByTagName(tag);
        s = stIdNode.item(0).getChildNodes().item(0).getNodeValue();

        return s;
    }

    // 리스트의 개수를 반환
    public int getLength() {
        int len = 0;
        NodeList nodeList = doc.getElementsByTagName("itemList");
        len = nodeList.getLength();

        return len;
    }

    // 인덱스 반환
    // @param tag : 인덱스를 찾을 value의 tag
    // @param value : 인덱스를 찾을 value
    // @return : 인덱스 값, 없다면 -1
    public int index(String tag, String value){
        int indexNum = -1;

        for(int i = 0; i < getLength(); i++){
            NodeList nodeList = doc.getElementsByTagName("itemList");
            Node node = nodeList.item(i);
            Element fstElmnt = (Element) node;
            NodeList stIdNode = fstElmnt.getElementsByTagName(tag);
            if(value.equals(stIdNode.item(0).getChildNodes().item(0).getNodeValue())){
                indexNum = i;
                break;
            }
        }
        return indexNum;
    }
}
