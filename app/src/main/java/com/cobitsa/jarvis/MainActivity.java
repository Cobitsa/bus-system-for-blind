package com.cobitsa.jarvis;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
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
    private Button sttButton;
    private static Context context;
    private String key = "mpXr6l%2BzwqmC6m4%2B%2FXEuhxPp62Z0EthgawICAoV%2BxIv4OKFnU53i2bH2omhogoZ3a4HWK1uK3Uq8WpJxn2k3WQ%3D%3D";
    public static UserData userData = new UserData();
    public static SetRideBus rideBus;
    public static SetDestination setDestination;
    public static TextView startStTextView;
    public static TextView startStIdTextView;
    public static TextView busTextView;
    public static TextView destStTextView;
    public static TextView destStIdTextView;
    public static Animation anim;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
        setContentView(R.layout.activity_main);
        command = new Command(mainActivity);
        rideBus = new SetRideBus(key);
        setDestination = new SetDestination(key);
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        startStTextView = findViewById(R.id.currentStTextView);
        startStIdTextView = findViewById(R.id.currentStIdTextView);
        busTextView = findViewById(R.id.busTextView);
        destStTextView = findViewById(R.id.destStTextView);
        destStIdTextView = findViewById(R.id.destStIdTextView);
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }

        // 애니메이션 설정
        anim = new AlphaAnimation(0.0f,1.0f);
        anim.setDuration(100);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(20);

        command = new Command(mainActivity);

        // STT버튼 OnTouch 이벤트 (색상변경)
        sttButton = findViewById(R.id.STTButton);
        sttButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                command.getCommand();
            }
        });
        sttButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        sttButton.setBackgroundResource(R.drawable.sttbutton_clicked);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        sttButton.setBackgroundResource(R.drawable.sttbutton);
                        break;
                    }
                }
                return  false;
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