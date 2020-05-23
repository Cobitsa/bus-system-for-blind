package com.cobitsa.jarvis.com.cobitsa.jarvis.voice;

import android.app.Activity;

import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.UserData;
import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.common.GetPrevStId;
import com.cobitsa.jarvis.com.cobitsa.jarvis.bus.ride.SetRideBus;

import java.util.ArrayList;
import java.util.List;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;

public class Command {
    private STT stt;
    TTS tts;
    Komoran komoran;
    ArrayList<String> args = new ArrayList<String>();
    int commandFlag = 0;
    int cnt = 0;
    public static OnEndListeningListener onEndListeningListener = null;

    public void setOnEndListeningListener(OnEndListeningListener listener) {
        onEndListeningListener = listener;
    }

    public Command(Activity activity) {
        stt = new STT(activity);
        this.komoran = new Komoran(DEFAULT_MODEL.LIGHT);
        tts = new TTS();

        setOnEndListeningListener(new OnEndListeningListener() {
            @Override
            public void onEndListening(ListeningEvent listeningEvent) throws InterruptedException {
                if (cnt == 0)                       // Speech Recognizer onResult 두번 호출되는 오류때문에 핸들링
                    executeCommand(stt.result.get(0));
                else
                    cnt--;
            }
        });
    }

    public void getCommand(){
        stt.startListening();
    }

    public KomoranResult analyzeCommand(String command) {
        return this.komoran.analyze(command);

    }

    public void executeCommand(String command) throws InterruptedException {
        KomoranResult analyzeResult = analyzeCommand(command);
        List<String> verb = analyzeResult.getMorphesByTags("VV");
        if (verb.contains("타")) {
            //탑승할 버스 지정
            System.out.println("탑승할 버스 노선 지정");
            String[] split = command.split("번");
            args.add(split[0]);
            tts.speech(split[0] + "번 버스가 맞습니까?");
            Thread.sleep(2000);
            commandFlag = 1;
            //맞는지아닌지 확인
            this.cnt = 1;
            getCommand();
        } else if (verb.contains("내리")) {
            // 내릴 정류장 지정
            System.out.println("하차할 정류장 지정");
            String[] split = command.split("에서");
            args.add(split[0]);
            tts.speech(split[0] + "이 맞습니까?");
            Thread.sleep(2000);
            commandFlag = 2;
            this.cnt = 1;
            //맞는지 아닌지 확인
            getCommand();
        } else if (analyzeResult.getMorphesByTags("NP").contains("여기")) {
            // 현재 정류장 확인
            tts.speech("현재 정류장 정보를 알려드릴게요");
        } else if (command.contains("그래") || command.contains("어") || command.contains("네") || command.contains("응") || command.contains("맞아")) {
            if (commandFlag == 1) {
                // 탑승지정 명령 실행
                tts.speech(this.args.get(0) + "번 버스가 오면 알려드릴게요");
            } else if (commandFlag == 2) {
                // 하차지정 명령 실행
                tts.speech(this.args.get(0)+ "에서 알려드릴게요");
            }
            commandFlag = 0;
            args.clear();
        } else if (command.contains("아니")) {
            tts.speech("명령을 다시 내려주세요");
            commandFlag = 0;
            args.clear();
        } else {
            tts.speech("잘 모르겠어요. 다시 한번 말씀해주세요.");
        }
        cnt = 1;
    }

    public void shutdownCommand() {
        stt.shutdownSTT();
        tts.shutdownTTS();
    }
}
