package com.cobitsa.jarvis.com.cobitsa.jarvis.voice;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.cobitsa.jarvis.MainActivity;

import java.util.ArrayList;

import static androidx.core.app.ActivityCompat.requestPermissions;

public class STT {
    Intent intent;
    SpeechRecognizer mRecognizer;
    final Context context;
    static ArrayList<String> result = new ArrayList<String>();

    public STT(Activity activity) {
        this.context = MainActivity.getAppContext();
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        mRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                Toast.makeText(context, "지금부터 말을 해주세요!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                System.out.println("입력 종료");
            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                    String key = "";
                    key = SpeechRecognizer.RESULTS_RECOGNITION;
                    result = bundle.getStringArrayList(key);
                    String[] rs = new String[result.size()];
                    result.toArray(rs);
                    Toast.makeText(context, rs[0], Toast.LENGTH_SHORT).show();
                    try {
                        Command.onEndListeningListener.onEndListening(new ListeningEvent(this));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }

    public void startListening(){
        mRecognizer.startListening(intent);
    }


    public void shutdownSTT() {
        if (mRecognizer != null) {
            mRecognizer.destroy();
            mRecognizer.cancel();
            mRecognizer = null;
        }
    }
}
