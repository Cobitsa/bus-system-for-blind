package com.cobitsa.jarvis;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.cobitsa.jarvis.com.cobitsa.jarvis.voice.STT;

public class MainActivity extends AppCompatActivity {
    private Activity mainActivity = this;
    private STT stt;
    private Button sttButton;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
        setContentView(R.layout.activity_main);
        stt = new STT();
        sttButton = (Button) findViewById(R.id.STTButton);
        sttButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stt.getCommand(mainActivity);
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
