package com.cobitsa.jarvis;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.ride.setRideBus;
import com.cobitsa.jarvis.com.cobitsa.jarvis.voice.Command;
import com.cobitsa.jarvis.com.cobitsa.jarvis.voice.TTS;

public class MainActivity extends AppCompatActivity {
    private Activity mainActivity = this;
    private Command command;
    private TTS tts;
    private Button sttButton;
    private static Context context;

    String stId = "20004";
    String busNumber = "150";
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();

        tts = new TTS();

        setContentView(R.layout.activity_main);
        command = new Command(mainActivity);
        sttButton = (Button) findViewById(R.id.STTButton);
        sttButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    command.getCommand();
            }
        });

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        data = setRideBus.isThereBus(stId, busNumber);
                        if (data == null)
                        {
                            tts.speech("해당 정류장에 존재하지 않는 버스 번호 입니다.");
                        }
                        else
                        {
                            tts.speech("버스 아이디는 " + data + "입니다.");
                        }
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
