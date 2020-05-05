package com.cobitsa.jarvis;


import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cobitsa.jarvis.com.cobitsa.jarvis.voice.TTS;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common.findBusStop;
import com.cobitsa.jarvis.com.cobitsa.jarvis.voice.STT;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {
    private Activity mainActivity = this;
    private findBusStop findBusStop;

    private TTS tts;
    private STT stt;
    private Button sttButton;
    TextView textView;
    double xLocation;
    double yLocation;
    String stId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        tts = new TTS(getApplicationContext());
        stt = new STT(getApplicationContext());
        sttButton = (Button) findViewById(R.id.STTButton);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLocationService();
                //stId = findBusStop.callAPI(xLocation, yLocation);
            }
        });

        sttButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stt.getCommand(mainActivity);
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
                String message = "최근 위치 -> Latitude : " + latitude + " \nLongitude:" + longitude;

                textView.setText(message);
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

            String message = "내 위치 -> Latitude : "+ latitude + "\nLongitude:"+ longitude;
            textView.setText(message);
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
        stt.shutdownSTT();
        super.onDestroy();
    }
}
