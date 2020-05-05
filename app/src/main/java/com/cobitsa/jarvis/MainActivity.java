package com.cobitsa.jarvis;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.ride.setRideBus;
import com.cobitsa.jarvis.com.cobitsa.jarvis.voice.STT;

public class MainActivity extends AppCompatActivity {
    private Activity mainActivity = this;
    private STT stt;
    private Button sttButton;
    String stId;
    String busNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stt = new STT(getApplicationContext());
        sttButton = (Button) findViewById(R.id.STTButton);
        sttButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stt.getCommand(mainActivity);
            }
        });

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRideBus.isThereBus(stId, busNumber);
            }
        });
    }

    @Override
    protected void onDestroy() {
        stt.shutdownSTT();
        super.onDestroy();
    }
}
