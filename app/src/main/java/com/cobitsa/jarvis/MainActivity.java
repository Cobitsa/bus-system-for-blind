package com.cobitsa.jarvis;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.UserData;
import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common.GetPrevStId;
import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common.TraceBus;
import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.getoff.SetDestination;
import com.cobitsa.jarvis.com.cobitsa.jarvis.voice.Command;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Activity mainActivity = this;
    private Command command;
    private Button rideButton;
    private Button getOffButton;
    private static Context context;
    private String key = "mpXr6l%2BzwqmC6m4%2B%2FXEuhxPp62Z0EthgawICAoV%2BxIv4OKFnU53i2bH2omhogoZ3a4HWK1uK3Uq8WpJxn2k3WQ%3D%3D";
    UserData userData = new UserData();
    GetPrevStId getPrevStId = new GetPrevStId(key);
    TraceBus traceBus = new TraceBus(key);
    SetDestination setDestination = new SetDestination(key);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
        setContentView(R.layout.activity_main);
        command = new Command(mainActivity);
        rideButton = (Button) findViewById(R.id.RideButton);
        getOffButton = (Button) findViewById(R.id.GetOffButton);
        rideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                command.getCommand();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Simulator 1 : 현재 정류소 지정
                        userData.startStation.name = "숭실대별관앞";
                        userData.startStation.id = "119000078";
                        userData.startStation.arsId = "20171";

                        // Simulator 2 : 탑승예정 버스 지정
                        userData.ridingBus.number = "752";
                        userData.ridingBus.routeId = "100100117";
                        userData.ridingBus.vehId = "111033037";

                        // Simulator 3 : 이전 정류소 정보 검색
                        userData.startStation.prevId = getPrevStId.get(userData.ridingBus.routeId, userData.startStation.id);

                        // Simulator 4 : 탑승예정 버스 추적
                        // traceBus.tracing(userData.startStation.prevId, userData.ridingBus.vehId, 1);
                    }
                }).start();
            }
        });


        getOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                command.getCommand();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Simulator 5 : 도착 정류소 지정
                        ArrayList<String> list = new ArrayList<>();
                        list = setDestination.set(userData.ridingBus.routeId, userData.startStation.arsId, "총신대");
                        userData.desStation.name = list.get(0);
                        userData.desStation.id = list.get(1);
                        userData.desStation.arsId = list.get(2);

                        // Simulator 6 : 이전 정류소 정보 검색
                        userData.desStation.prevId = getPrevStId.get(userData.ridingBus.routeId, userData.desStation.id);
                        System.out.println(userData.desStation.prevId);

                        // Simulator 7 : 탑승중인 버스 추적
                        //traceBus.tracing(userData.desStation.prevId, userData.ridingBus.vehId, 2);
                    }
                }).start();
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

