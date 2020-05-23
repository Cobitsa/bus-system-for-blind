package com.cobitsa.jarvis;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.UserData;
import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common.GetPrevStId;
import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common.GpsTracker;
import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common.TraceBus;
import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.getoff.SetDestination;
import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.ride.GetStationInfo;
import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.ride.SetRideBus;
import com.cobitsa.jarvis.com.cobitsa.jarvis.voice.Command;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Activity mainActivity = this;
    private Command command;
    private Button rideButton;
    private Button getOffButton;
    private Button gpsButton;
    private static Context context;
    private String key = "mpXr6l%2BzwqmC6m4%2B%2FXEuhxPp62Z0EthgawICAoV%2BxIv4OKFnU53i2bH2omhogoZ3a4HWK1uK3Uq8WpJxn2k3WQ%3D%3D";
    UserData userData = new UserData();
    GpsTracker gpsTracker;
    GetStationInfo getStationInfo = new GetStationInfo(key);
    SetRideBus rideBus = new SetRideBus(key);
    GetPrevStId getPrevStId = new GetPrevStId(key);
    TraceBus traceBus = new TraceBus(key);
    SetDestination setDestination = new SetDestination(key);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
        setContentView(R.layout.activity_main);
        command = new Command(mainActivity);
        rideButton = findViewById(R.id.RideButton);

        // TEST
        // 현재 정류소 지정 및 탑승예정 버스 지정 후 추적
        // 위 과정을 test 하기위한 버튼
        rideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                command.getCommand();

                // Process 0 : GPS값 세팅
                // GPS 좌표값을 불러온다
                gpsTracker = new GpsTracker(MainActivity.this);
                final double latitute = gpsTracker.getLatitude();
                final double longitude = gpsTracker.getLongitude();

                new Thread(new Runnable() {
                    private String tmX = Double.toString(latitute);
                    private String tmY = Double.toString(longitude);
                    @Override
                    public void run() {
                        // Process 1 : 현재 정류소 지정
                        // 현재 정류소의 이름, 아이디, 고유번호 저장
                        // 현재 GPS 값 (126.9602929082, 37.4946390733)로 가정 (숭실대별관 정류소 GPS값)
                        tmX = "126.9602929082";
                        tmY = "37.4946390733";
                        String radius = "30";
                        ArrayList<String> stInfo;
                        stInfo = getStationInfo.get(tmX, tmY, radius);
                        userData.startStation.id = stInfo.get(0);
                        userData.startStation.name = stInfo.get(1);
                        userData.startStation.arsId = stInfo.get(2);

                        // Process 2-1 : 탑승예정 버스 지정
                        // STT로 "752번"를 입력받았다고 가정
                        // 탑승 예정 버스의 번호, 노선 아이디 저장
                        ArrayList<String> rideInfo;
                        rideInfo = rideBus.set(userData.startStation.arsId, "752");
                        userData.ridingBus.number = rideInfo.get(0);
                        userData.ridingBus.routeId = rideInfo.get(1);

                        // Process 2-2 : 탑승예정 버스 지정
                        // 탑승 예정 버스의 차량 아이디 저장
                        userData.ridingBus.vehId = rideBus.setVehId(userData.startStation.id, userData.ridingBus.routeId);

                        // Process 3 : 버스 추적을 위한 이전 정류소 정보 검색
                        userData.startStation.prevId = getPrevStId.get(userData.ridingBus.routeId, userData.startStation.id);

//                         // Process 4 : 탑승예정 버스 추적
//                        traceBus.tracing(userData.startStation.prevId, userData.ridingBus.vehId, 1);
                    }
                }).start();
            }
        });

        // TEST
        // 도착 정류소 지정 및 탑승중인 버스 추적
        // 위 과정을 test 하기위한 버튼
        getOffButton = findViewById(R.id.GetOffButton);
        getOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                command.getCommand();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Process 5 : 도착 정류소 지정
                        // 도착 정류소의 이름, 아이디, 고유번호 저장
                        // TTS로 "총신대에서 내릴꺼야" 받았다고 가정
                        ArrayList<String> destInfo;
                        destInfo = setDestination.set(userData.ridingBus.routeId, userData.startStation.arsId, "총신대");
                        userData.desStation.name = destInfo.get(0);
                        userData.desStation.id = destInfo.get(1);
                        userData.desStation.arsId = destInfo.get(2);

                        // Process 6 : 버스 추적을 위한 이전 정류소 정보 검색
                        userData.desStation.prevId = getPrevStId.get(userData.ridingBus.routeId, userData.desStation.id);

                        // Process 7 : 탑승중인 버스 추적
                        //traceBus.tracing(userData.desStation.prevId, userData.ridingBus.vehId, 2);
                    }
                }).start();
            }
        });

        // TEST
        // GPS 좌표값을 얻은후에 출력
        // 위 과정을 test 하기위한 버튼
        gpsButton = findViewById(R.id.GPSButton);
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                command.getCommand();
                gpsTracker = new GpsTracker(MainActivity.this);
                double latitute = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                System.out.println("위도는 : " + latitute);
                System.out.println("경도는 : " + longitude);
            }
        });

    }



    @Override
    protected void onDestroy() {
        command.shutdownCommand();
        super.onDestroy();
    }

    public static Context getAppContext(){
        return MainActivity.context;
    }
}

