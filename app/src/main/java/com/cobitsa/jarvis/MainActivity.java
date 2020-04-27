package com.cobitsa.jarvis;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.cobitsa.jarvis.com.cobitsa.jarvis.voice.TTS;

public class MainActivity extends AppCompatActivity{

    // TTS 호출 예제
    private TTS tts;
    private Button btnEnter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tts = new TTS(getApplicationContext());
        setContentView(R.layout.activity_main);
        btnEnter = (Button) findViewById(R.id.button);
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speech("안녕하세요?");
            }
        });
    }
    @Override
    protected void onDestroy() {
        tts.shutdownTTS();
        super.onDestroy();
    }
}
