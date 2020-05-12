package com.cobitsa.jarvis;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.cobitsa.jarvis.com.cobitsa.jarvis.voice.Command;

public class MainActivity extends AppCompatActivity {
    private Activity mainActivity = this;
    private Command command;
    private Button sttButton;
    private static Context context;

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