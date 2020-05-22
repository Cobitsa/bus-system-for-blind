package com.cobitsa.jarvis.com.cobitsa.jarvis.bus;

public class UserData {

    public Bus ridingBus;
    public Station startStation;
    public Station desStation;

    public  UserData(){
        startStation = new Station();
        desStation = new Station();
        ridingBus = new Bus();
    }

    public static class Bus {
        public String number;
        public String vehId;
        public String routeId;

        public Bus(){
            number = "";
            vehId = "";
            routeId = "";
        }
    }

    public static class Station{
        public String name;
        public String id;
        public String arsId;
        public String prevId;

        public Station(){
            name = "";
            id = "";
            arsId = "";
            prevId = "";
        }
    }
}
