package com.cobitsa.jarvis;


import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cobitsa.jarvis.com.cobitsa.jarvis.voice.Command;
import com.cobitsa.jarvis.com.cobitsa.jarvis.voice.TTS;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

public class MainActivity extends AppCompatActivity  implements AutoPermissionsListener{
    private Activity mainActivity = this;
    private Command command;
    private Button sttButton;
    private static Context context;

    private TTS tts;

    double xLocation;
    double yLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
        setContentView(R.layout.activity_main);
        command = new Command(mainActivity);

        tts = new TTS();
        sttButton = (Button) findViewById(R.id.STTButton);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLocationService();
            }
        });

        sttButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    command.getCommand();
            }
        });

        AutoPermissions.Companion.loadAllPermissions(this, 101);
    }
    public void startLocationService() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try{
            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null){
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                xLocation = latitude;
                yLocation = longitude;
                String message = "나의 위치는 X 좌표" + latitude + " Y 좌표" + longitude + "입니다.";

                tts.speech(message);
            }

            GPSListener gpsListener = new GPSListener();
            long minTime = 10000;
            float minDistance = 0;

            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime, minDistance, gpsListener);

            Toast.makeText(getApplicationContext(), "내 위치확인 요청함",
                    Toast.LENGTH_SHORT).show();
        }
        catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();
        }

        public void onProviderDisabled(String provider) { }

        public void onProviderEnabled(String provider) { }

        public void onStatusChanged(String provider, int status, Bundle extras) { }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }

    public void onDenied(int requestCode, @NonNull String[] permissions) {
        Toast.makeText(this, "permissions denied : " + permissions.length, Toast.LENGTH_LONG).show();
    }

    public void onGranted(int requestCode, @NonNull String[] permissions) {
        Toast.makeText(this, "permissions granted : " + permissions.length, Toast.LENGTH_LONG).show();
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
