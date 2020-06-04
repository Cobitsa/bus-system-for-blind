package com.cobitsa.jarvis;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.UserData;
import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.getoff.SetDestination;
import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.ride.SetRideBus;
import com.cobitsa.jarvis.com.cobitsa.jarvis.voice.Command;

public class MainActivity extends AppCompatActivity {
    private Activity mainActivity = this;
    private Command command;
    public static Vibrator vibrator;
    private ImageButton sttButton;
    private static Context context;
    private String key = "mpXr6l%2BzwqmC6m4%2B%2FXEuhxPp62Z0EthgawICAoV%2BxIv4OKFnU53i2bH2omhogoZ3a4HWK1uK3Uq8WpJxn2k3WQ%3D%3D";
    public static UserData userData = new UserData();
    public static SetRideBus rideBus;
    public static SetDestination setDestination;
    public static TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
        setContentView(R.layout.activity_main);
        command = new Command(mainActivity);
        rideBus = new SetRideBus(key);
        setDestination = new SetDestination(key);
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        textView = findViewById(R.id.currentStTextView);
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }



        command = new Command(mainActivity);
        sttButton = findViewById(R.id.STTButton);
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

