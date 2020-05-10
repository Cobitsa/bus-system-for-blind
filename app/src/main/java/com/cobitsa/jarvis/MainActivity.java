package com.cobitsa.jarvis;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cobitsa.jarvis.com.cobitsa.jarvis.voice.STT;
import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common.TraceBus;

public class MainActivity extends AppCompatActivity {
    private Activity mainActivity = this;
    private STT stt;
    private Button sttButton;
    private static Context context;

    String key = "";
    String stId = "";
    String busRouteId = "";
    String vehId = "";



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
                TraceBus traceBus = new TraceBus(key, stId, busRouteId, vehId);
                if(traceBus.tracing()) {
                    Toast.makeText(context, "Test Text", Toast.LENGTH_SHORT).show();
                }
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
