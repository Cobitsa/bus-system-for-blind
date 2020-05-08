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
import java.util.List;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;

import static androidx.core.app.ActivityCompat.requestPermissions;

public class STT {
    Intent intent;
    SpeechRecognizer mRecognizer;
    Komoran komoran;
    final Context context;
    ArrayList<String> result = new ArrayList<String>();
    Boolean isResult = true;
    TTS tts;
    Activity activity;

    public STT() {
        this.context = MainActivity.getAppContext();
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        this.komoran = new Komoran(DEFAULT_MODEL.FULL);
        tts = new TTS();
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
                if (isResult) { //google SpeechRecognizer의 onResults가 두번 실행되는 오류때문에 핸들링
                    String key = "";
                    key = SpeechRecognizer.RESULTS_RECOGNITION;
                    result = bundle.getStringArrayList(key);
                    String[] rs = new String[result.size()];
                    result.toArray(rs);
                    Toast.makeText(context, rs[0], Toast.LENGTH_SHORT).show();

                    System.out.println(result.get(0));
                    executeCommand(result.get(0));
                    isResult = false;
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

    public void getCommand(Activity activity) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            //권한을 허용하지 않는 경우
        } else {
            //권한을 허용한 경우
            try {
                this.isResult = true;
                this.activity = activity;
                mRecognizer.startListening(intent);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    public KomoranResult analyzeCommand(String command) {
        return this.komoran.analyze(command);

    }

    public Boolean executeCommand(String command) {
        KomoranResult analyzeResult = analyzeCommand(command);
        List<String> verb = analyzeResult.getMorphesByTags("VV");
        if (verb.contains("타")) {
            //탑승할 버스 지정
            System.out.println("탑승할 버스 노선 지정");
            String[] split = command.split("번");
            tts.speech(split[0] + "번 버스가 맞습니까?");
        } else if (verb.contains("내리")) {
            // 내릴 정류장 지정
            System.out.println("하차할 정류장 지정");
            String[] split = command.split("에서");
            tts.speech(split[0] + "이 맞습니까?");
        } else if (analyzeResult.getMorphesByTags("NP").contains("여기")) {
            // 현재 정류장 확인
            System.out.println("현재 정류장 위치 확인");
        } else if (command.contains("네") || command.contains("응")) {
            return true;
        } else if (command.contains("아니")) {
            return false;
        } else {
            tts.speech("잘 모르겠어요. 다시 한번 말씀해주세요.");
        }
        return true;
    }

    public void shutdownSTT() {
        if (mRecognizer != null) {
            mRecognizer.destroy();
            mRecognizer.cancel();
            mRecognizer = null;
        }
    }
}
