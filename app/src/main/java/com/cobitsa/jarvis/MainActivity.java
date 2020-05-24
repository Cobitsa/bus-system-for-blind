package com.cobitsa.jarvis;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.UserData;
import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common.GetPrevStId;
import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common.GpsTracker;
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
    public static Vibrator vibrator;
    private Button sttButton;
    private static Context context;
    private String key = "mpXr6l%2BzwqmC6m4%2B%2FXEuhxPp62Z0EthgawICAoV%2BxIv4OKFnU53i2bH2omhogoZ3a4HWK1uK3Uq8WpJxn2k3WQ%3D%3D";
    public static UserData userData = new UserData();
    GpsTracker gpsTracker;
    GetStationInfo getStationInfo = new GetStationInfo(key);
    public static SetRideBus rideBus;
    GetPrevStId getPrevStId = new GetPrevStId(key);
    public static SetDestination setDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
        setContentView(R.layout.activity_main);
        command = new Command(mainActivity);
        rideButton = findViewById(R.id.RideButton);
        rideBus = new SetRideBus(key);
        setDestination = new SetDestination(key);
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }

        // TEST
        // 현재 정류소 지정 및 탑승예정 버스 지정 후 추적
        // 위 과정을 test 하기위한 버튼
        rideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Process 1 : 현재 정류소 지정
                // 현재 정류소의 이름, 아이디, 고유번호 저장
                // 현재 GPS 값 (126.9602929082, 37.4946390733)로 가정 (숭실대별관 정류소 GPS값)
                String tmX = "126.9602929082";
                String tmY = "37.4946390733";
                String radius = "30";
                ArrayList<String> stInfo;
                stInfo = getStationInfo.getApiData(tmX, tmY, radius);
                userData.setStartStation(stInfo.get(0), stInfo.get(1), stInfo.get(2));

                System.out.println(stInfo.get(0));
                System.out.println(stInfo.get(1));
                System.out.println(stInfo.get(2));
                // Process 2-1 : 탑승예정 버스 지정
                // STT로 "752번"를 입력받았다고 가정
                // 탑승 예정 버스의 번호, 노선 아이디 저장
                ArrayList<String> rideInfo;
                rideBus.setBus(userData.startStation.arsId, "752");
            }
        });

        // TEST
        // 도착 정류소 지정 및 탑승중인 버스 추적
        // 위 과정을 test 하기위한 버튼
        getOffButton = findViewById(R.id.GetOffButton);
        getOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Process 5 : 도착 정류소 지정
                // 도착 정류소의 이름, 아이디, 고유번호 저장
                // TTS로 "총신대에서 내릴꺼야" 받았다고 가정
                ArrayList<String> destInfo;
                setDestination.setBus(userData.ridingBus.routeId, userData.startStation.arsId, "총신대");

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

        command = new Command(mainActivity);
        sttButton = (Button) findViewById(R.id.STTButton);
        sttButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                command.getCommand();
            }
        });
    }

    @Override
    protected void onDestroy() {
        command.shutdownCommand();
        super.onDestroy();
    }

    public static Context getAppContext() {
        return MainActivity.context;
    }

}

