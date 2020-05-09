package com.cobitsa.jarvis.com.cobitsa.jarvis.voice;

import java.util.EventListener;

public interface OnEndListeningListener extends EventListener {
    void onEndListening(ListeningEvent listeningEvent) throws InterruptedException;

}