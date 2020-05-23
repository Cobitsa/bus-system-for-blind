package com.cobitsa.jarvis.com.cobitsa.jarvis.bus;

// 버스 및 정류소의 정보들을 저장해둘 객체
public class UserData {

    public Bus ridingBus;               // 탑승예정 또는 중인 버스
    public static Station startStation;     // 버스를 탑승할 또는 한 정류소
    public Station desStation;       // 목적지 정류소

    public UserData() {
        startStation = new Station();
        desStation = new Station();
        ridingBus = new Bus();
    }

    public void setStartStation(String id, String name, String arsId) {
        this.startStation.name = name;
        this.startStation.id = id;
        this.startStation.arsId = arsId;
    }

    public void setDestStation(String id, String name, String arsId) {
        this.desStation.name = name;
        this.desStation.id = id;
        this.desStation.arsId = arsId;
    }

    public class Bus {
        public String number;       // 버스 번호
        public String routeId;        // 노선 아이디
        public String vehId;           // 차량 아이디

        public Bus() {
            number = "";
            routeId = "";
            vehId = "";
        }
    }

    public class Station {
        public String name;         // 정류소 이름
        public String id;               // 정류소 아이디
        public String arsId;         // 정류소 고유번호
        public String prevId;       // 이전 정류소 아이디

        public Station() {
            name = "";
            id = "";
            arsId = "";
            prevId = "";
        }
    }
}
