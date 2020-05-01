package com.cobitsa.jarvis;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.cobitsa.jarvis.com.cobitsa.jarvis.voice.STT;

public class MainActivity extends AppCompatActivity {
    private Activity mainActivity = this;
    private STT stt;
    private Button sttButton;

    // TTS 호출 예제
    private TTS tts;
    private Button btnEnter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tts = new TTS(getApplicationContext());
        setContentView(R.layout.activity_main);
        stt = new STT(getApplicationContext());
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
}
