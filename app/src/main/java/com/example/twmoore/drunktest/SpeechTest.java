package com.example.twmoore.drunktest;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by twmoore on 4/27/2017.
 */

public class SpeechTest extends AppCompatActivity {
    private TextView sentenceTextView;
    private TextView userSpeechTextView;

    private final int SPEECH_INPUT_CODE = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speech_test);

        sentenceTextView = (TextView) findViewById(R.id.sentence);
        userSpeechTextView = (TextView) findViewById(R.id.userVoiceInput);
    }

    public void startVoiceRecognition(View view) {
        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        // can add an intent to compare string to something here

        try {
                startActivityForResult(speechIntent, SPEECH_INPUT_CODE);
        } catch (ActivityNotFoundException ex) {
            Toast notSupportedToast = Toast.makeText(getApplicationContext(),
                                    "Speech input not supported", Toast.LENGTH_SHORT);
            notSupportedToast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_INPUT_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> speechResults = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String lowerCaseText = speechResults.get(0).toLowerCase();
            userSpeechTextView.setText(lowerCaseText);

            int accuracyFloor = 80;
            if (accuracyPassed(accuracyFloor, lowerCaseText)) {
                Toast passedToast = Toast.makeText(getApplicationContext(),
                                    "You didn't slur!", Toast.LENGTH_SHORT);
                passedToast.show();
            } else {
                Toast drunkToast = Toast.makeText(getApplicationContext(),
                                    "You're drunk!", Toast.LENGTH_SHORT);
                drunkToast.show();
            }
        }
    }

    private Boolean accuracyPassed(int accuracyThreshold, String speechText) {
        if (accuracyThreshold < 0 || accuracyThreshold > 100) {
            return false;
        }
        String lowerCaseSentenceText = sentenceTextView.getText().toString().toLowerCase();
        String[] sentenceWords = lowerCaseSentenceText.split(" ");

        int sameWordCount = countSameWords(sentenceWords, speechText);
        int sentenceLength = sentenceWords.length;
        double percentage = 100.0 * sameWordCount / sentenceLength;

        if (percentage >= accuracyThreshold) {
            return true;
        } else {
            return false;
        }
    }

    private int countSameWords(String[] sentenceWords, String two) {
        String[] stringTwoArr = two.split(" ");

        int sameWordCount = 0;

        for (int i = 0; i < sentenceWords.length &&  i < stringTwoArr.length; i++) {
            if (sentenceWords[i].equals(stringTwoArr[i])) {
                sameWordCount++;
            }
        }
        Log.v("COUNT", Integer.toString(sameWordCount));
        return sameWordCount;
    }
}
