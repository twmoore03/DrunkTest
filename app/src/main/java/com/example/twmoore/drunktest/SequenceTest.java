package com.example.twmoore.drunktest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by twmoore on 4/27/2017.
 */

public class SequenceTest extends AppCompatActivity {
    private TextView sequenceNumberView;
    private Deque<Integer> sequenceQueue;
    Timer timer;

    private int currentSequenceCount;
    private int sequenceLength;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sequence_test);

        sequenceNumberView = (TextView) findViewById(R.id.sequenceNumber);
        sequenceQueue = new ArrayDeque<>();

        currentSequenceCount = 0;
        sequenceLength = 4;

    }

    private void updateSequenceNumber() {
        int sequenceNumber = getRandomNumber(0, 20);
        if (sequenceQueue.size() < sequenceLength) {
            sequenceQueue.add(sequenceNumber);
        }
        sequenceNumberView.setText(Integer.toString(sequenceNumber));
    }

    private int getRandomNumber(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max) + min;
    }

    public void checkAnswer(View view) {
        EditText userInput = (EditText) findViewById(R.id.inputAnswer);
        Editable userAnswer = userInput.getText();
        int numberInput = 0;
        if (userAnswer == null || userAnswer.toString().equals("")) {
            Toast emptyToast = Toast.makeText(getApplicationContext(), "Put in an answer!", Toast.LENGTH_SHORT);
            emptyToast.show();
        } else {
            numberInput = Integer.valueOf(userAnswer.toString());

            // check queue each time, if same pop, if not toast that you failed
            if (numberInput == sequenceQueue.pop()) {
                userAnswer.clear();
                Toast numbersLeftToast = Toast.makeText(getApplicationContext(),
                        "You have " + sequenceQueue.size() + " numbers left", Toast.LENGTH_SHORT);
                numbersLeftToast.show();
                if (sequenceQueue.size() == 0) {
                    Toast successToast =  Toast.makeText(getApplicationContext(), "You passed 3rd grade!", Toast.LENGTH_LONG);
                    successToast.show();
                }
            } else {
                userAnswer.clear();
                Toast failToast = Toast.makeText(getApplicationContext(), "You're Drunk!!", Toast.LENGTH_LONG);
                failToast.show();
            }
        }



    }

    public void beginSequence(View view) {
        currentSequenceCount = 0;
        sequenceQueue.clear();

        if (timer != null) {
            timer = null;
        }
        timer = new Timer();

        TimerTask sequenceTask = new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateSequenceNumber();
                        incrementSequenceCount();
                        sequenceCountCheck();
                    }
                });
            }
        };

        timer.schedule(sequenceTask, 1000, 1000);
    }

    private void incrementSequenceCount() {
        if (currentSequenceCount <= sequenceLength) {
            currentSequenceCount++;
        }
    }

    private void sequenceCountCheck() {
        if (currentSequenceCount > sequenceLength) {
            timer.cancel();
            sequenceNumberView.setText("");
        }
    }

}
