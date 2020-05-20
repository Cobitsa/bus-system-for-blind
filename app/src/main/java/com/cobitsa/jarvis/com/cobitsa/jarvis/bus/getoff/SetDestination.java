package com.cobitsa.jarvis.com.cobitsa.jarvis.bus.getoff;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common.ParsingXML;

import javax.xml.parsers.ParserConfigurationException;

public class SetDestination {

    String key;                               // 서비스 키
    ArrayList<String> nameList;               // 정류소 이름 List
    ArrayList<String> idList;                 // 정류소 아이디 List

    // SetDestination 생성자
    // @param key = 서비스 키
    public SetDestination(String key) {
        this.key = key;
        nameList = new ArrayList<>();
        idList = new ArrayList<>();
    }

    // 메인 메소드
    // @param 버스 노선 ID, STT로 받은 정류소 명
    // @return 유효한 정류소인 경우 정류소 ID
    // @return 유효하지 않은 정류소인 경우 ""
    public String setDestination(String busRouteId, String nowArsId, String sttDestination) {
        String destinationId = "";      // return 값
        String tmpName = "";            // 임시 변수 1
        String tmpId = "";              // 임시 변수 2

        // API URL 생성
        final String url = "http://ws.bus.go.kr/api/rest/busRouteInfo/getStaionByRoute" +
                "?ServiceKey=" + key +
                "&busRouteId=" + busRouteId;

        try {

            // 파싱
            ParsingXML parsingXML = new ParsingXML(url);
            for(int i = 0; i < parsingXML.getLength(); i++) {
                tmpName = parsingXML.parsing("stationNm", i);
                tmpId += parsingXML.parsing("station", i);

                // Name과 ID 각각의 리스트에 저장
                nameList.add(tmpName);
                idList.add(tmpId);
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        // 유효한 정류소인지 확인
        if(isExist(sttDestination, nameList)) {
            // 현재 위치한 정류장 또는 탑승한 정류장 인덱스
            int nowIndex = getIndex(nowArsId, idList);
            // 도착지의 인덱스를 저장할 리스트 (같은 이름의 정류장이 2개인 경우가 있으므로 리스트로 관리)
            ArrayList<Integer> desIndex = new ArrayList<>();

            // 도착지의 인덱스를 리스트에 저장
            for(int index = 0; index < nameList.size(); index++) {
                if(nameList.get(index).equals(sttDestination)) {
                    desIndex.add(index);
                }
            }

            // 현재 또는 탑승한 정류장의 다음 정류장 위치 고려하여
            // 도착지 인덱스 리스트에서 맞는 값 선택하는 과정
            if(desIndex.size() == 1) {
                destinationId = idList.get(0);
            }
            else if(desIndex.size() == 2) {
                destinationId = idList.get(0);
                if(nowIndex > desIndex.get(0) && nowIndex < desIndex.get(1)) {
                    destinationId = idList.get(1);
                }
            }

        }

        return destinationId;
    }

    // STT로 입력받은 정류소 이름이 유효한 정유장인지 확인 후 인덱스 값 반환
    // @param STT로 입력받은 정류소, 정류소 이름 리스트
    // @return 유효하다면 인덱스 값, 유효하지 않다면 -1
    public boolean isExist(String sttDestination, ArrayList<String> nameList) {
        boolean ret = false;

        for(String name : nameList) {
            if(name.equals(sttDestination)) {
                ret = true;
                break;
            }
        }

        return ret;
    }

    // @param 정류소 ID, 정류소 ID 리스트
    // @return 유효하다면 인덱스 값, 유효하지 않다면 -1
    public int getIndex(String id, ArrayList<String> idList) {
        int index = -1;

        for(int i = 0; i < idList.size(); i++) {
            if(idList.get(i).equals(id)) {
                index = i;
                break;
            }
        }

        return index;
    }
}
