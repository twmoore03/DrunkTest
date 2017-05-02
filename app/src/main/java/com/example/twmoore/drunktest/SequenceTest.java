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
        int sequenceNumber = getNewRandomNumber(0, 10);

        if (sequenceQueue.size() < sequenceLength) {
            sequenceQueue.add(sequenceNumber);
            sequenceNumberView.setText(Integer.toString(sequenceNumber));
        }
    }

    private int getNewRandomNumber(int min, int max) {
        Random rand = new Random();
        int randomNumber = rand.nextInt(max) + min;

        if (sequenceQueue.size() == 0) {
            return randomNumber;
        }

        if (randomNumber != sequenceQueue.peek()) {
            return randomNumber;
        } else {
            return getNewRandomNumber(min, max);
        }
    }

    public void checkAnswer(View view) {
        EditText userEditedText = (EditText) findViewById(R.id.inputAnswer);
        Editable userAnswer = userEditedText.getText();

        String userInput;

        if (userAnswer == null || userAnswer.toString().equals("")) {
            Toast emptyToast = Toast.makeText(getApplicationContext(), "Put in an answer!", Toast.LENGTH_SHORT);
            emptyToast.show();
        } else {
            userInput = userAnswer.toString();

            for (int i = 0; i < userInput.length(); i++)  {
                int numberInput = userInput.charAt(i) - '0';
                checkQueueForAnswer(numberInput);
            }
            userAnswer.clear();
        }
    }

    private void checkQueueForAnswer(int numberInput) {
        if (numberInput == sequenceQueue.pop()) {
            if (sequenceQueue.size() == 0) {
                setResult(Constants.PASSED_TEST_CODE);
                finish();
            }
        } else {
            setResult(Constants.FAILED_TEST_CODE);
            finish();
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
                        sequenceCountCheck();
                        incrementSequenceCount();
                    }
                });
            }
        };

        timer.schedule(sequenceTask, 1000, 1000);
    }

    private void incrementSequenceCount() {
        if (currentSequenceCount < sequenceLength) {
            currentSequenceCount++;
        }
    }

    private void sequenceCountCheck() {
        if (currentSequenceCount == sequenceLength) {
            timer.cancel();
            sequenceNumberView.setText("");
        }
    }

}
