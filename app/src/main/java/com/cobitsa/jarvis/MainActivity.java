package com.cobitsa.jarvis;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Context;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common.getStationInfo;
import com.cobitsa.jarvis.com.cobitsa.jarvis.voice.Command;
import com.cobitsa.jarvis.com.cobitsa.jarvis.voice.STT;

public class MainActivity extends AppCompatActivity {
    private Activity mainActivity = this;

    private Command command;
    private STT stt;
    private Button sttButton;
    private static Context context;
    double tmx1;
    double tmy1;
    double tmx2;
    double tmy2;
    String stopId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
        setContentView(R.layout.activity_main);

        command = new Command(mainActivity);
        sttButton = (Button) findViewById(R.id.STTButton);
        sttButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                command.getCommand();
            }
        });

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                tmx1 = 126.95584930;
                tmy1 = 37.53843986;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getStationInfo info = new getStationInfo();
                        stopId = info.callAPI(tmx1, tmy1);

                    }
                }).start();
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                tmx2 = 37;
                tmy2 = -122;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getStationInfo info = new getStationInfo();
                        stopId = info.callAPI(tmx2, tmy2);
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        stt.shutdownSTT();
        super.onDestroy();
    }

    public static Context getAppContext(){
        return MainActivity.context;
    }
}
