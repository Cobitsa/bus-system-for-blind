package com.cobitsa.jarvis.com.cobitsa.jarvis.bus.getoff;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.UserData;
import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common.ParsingXML;

import javax.xml.parsers.ParserConfigurationException;

public class SetDestination {

    String key;                                                 // 서비스 키
    ArrayList<String> nameList;                 // 정류소 이름 List
    ArrayList<String> idList;                       // 정류소 아이디 List
    ArrayList<String> arsIdList;                 // 정류소 고유번호 List

    // SetDestination 생성자
    // @param key = 서비스 키
    public SetDestination(String key) {
        this.key = key;
        nameList = new ArrayList<>();
        idList = new ArrayList<>();
        arsIdList = new ArrayList<>();
    }

    // 메인 메소드
    // @param 버스 노선 ID, STT로 받은 정류소 명
    // @return 유효한 정류소인 경우 정류소 ID
    // @return 유효하지 않은 정류소인 경우 ""
    public ArrayList<String> set(String busRouteId, String startArsId, String sttDestination) {
        String desId = "";
        String desName = "";
        String desArs = "";
        String tmpName = "";            // 임시 변수 1
        String tmpId = "";                   // 임시 변수 2
        String tmpArs = "";                   // 임시 변수 3

        // API URL 생성
        final String url = "http://ws.bus.go.kr/api/rest/busRouteInfo/getStaionByRoute" +
                "?ServiceKey=" + key +
                "&busRouteId=" + busRouteId;

        try {
            ParsingXML parsingXML = new ParsingXML(url);
            for(int i = 0; i < parsingXML.getLength(); i++){
                tmpArs = parsingXML.parsing("arsId", i);
                tmpId = parsingXML.parsing("station", i);
                tmpName = parsingXML.parsing("stationNm", i);
                idList.add(tmpId);
                nameList.add(tmpName);
                arsIdList.add(tmpArs);
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
            ArrayList<Integer> indexList = new ArrayList<>();

            indexList.add(getIndex(startArsId, arsIdList));

            for(int index = 0; index < nameList.size(); index++) {
                if(nameList.get(index).equals(sttDestination)) {
                    indexList.add(index);
                }
            }

            // 현재 또는 탑승한 정류장의 다음 정류장 위치 고려하여
            // 도착지 인덱스 리스트에서 맞는 값 선택하는 과정
            if(indexList.size() == 2) {
                desId = idList.get(indexList.get(1));
                desName = nameList.get(indexList.get(1));
                desArs = arsIdList.get(indexList.get(1));
            }
            else if(indexList.size() == 3) {
                desId = idList.get(indexList.get(1));
                desName = nameList.get(indexList.get(1));
                desArs = arsIdList.get(indexList.get(1));
                if(indexList.get(0) > indexList.get(1) && indexList.get(0) < indexList.get(2)) {
                    desId = idList.get(indexList.get(2));
                    desName = nameList.get(indexList.get(2));
                    desArs = arsIdList.get(indexList.get(2));
                }
            }
        }

        ArrayList<String> refList = new ArrayList<>();
        refList.add(desName);
        refList.add(desId);
        refList.add(desArs);

        return refList;
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
    public int getIndex(String id, ArrayList<String> list) {
        int index = -1;

        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).equals(id)) {
                index = i;
                break;
            }
        }
        return index;
    }
}
