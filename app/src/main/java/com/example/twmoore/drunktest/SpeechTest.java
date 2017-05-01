package com.example.twmoore.drunktest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import static android.speech.SpeechRecognizer.createSpeechRecognizer;

/**
 * Created by twmoore on 4/27/2017.
 */

public class SpeechTest extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speech_test);

        // need to make a recognitionListener
        // need to make a speechRecognizer

        createSpeechRecognizer(this);
    }

    public void startVoiceRecognition(View view) {

    }
}
