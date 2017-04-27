package com.example.twmoore.drunktest;

import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by twmoore on 4/27/2017.
 */

public class SequenceTest extends AppCompatActivity {
    TextView sequenceNumberView;
    Deque<Integer> sequenceQueue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sequence_test);

        sequenceNumberView = (TextView) findViewById(R.id.sequenceNumber);
        sequenceQueue = new ArrayDeque<>();

    }

    private void updateSequenceNumber() {
        int sequenceNumber = getRandomNumber(0, 20);
        sequenceQueue.add(sequenceNumber);
        sequenceNumberView.setText(Integer.toString(sequenceNumber));
    }

    private int getRandomNumber(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max) + min;
    }

    public void checkAnswer(View view) {
        updateSequenceNumber();
    }

    public void beginSequence(View view) {
        Timer timer = new Timer();
        TimerTask sequenceTask = new TimerTask() {
            @Override
            public void run() {
                updateSequenceNumber();
            }
        };
    }
}
