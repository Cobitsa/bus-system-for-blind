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

    public String parsing(String tag, int index) {
        String s = "";
        NodeList nodeList = doc.getElementsByTagName("itemList");

        Node node = nodeList.item(index);
        Element fstElmnt = (Element) node;

        NodeList stIdNode = fstElmnt.getElementsByTagName(tag);
        s = stIdNode.item(0).getChildNodes().item(0).getNodeValue();

        return s;
    }

    public int getLength() {
        int len = 0;
        NodeList nodeList = doc.getElementsByTagName("itemList");
        len = nodeList.getLength();

        return len;
    }
}
