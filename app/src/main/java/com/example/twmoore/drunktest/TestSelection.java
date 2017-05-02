package com.example.twmoore.drunktest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class TestSelection extends AppCompatActivity {

    private final int MATH_TEST_REQ_CODE = 1;
    private final int SEQUENCE_TEST_REQ_CODE = 2;
    private final int SPEECH_TEST_REQ_CODE = 3;

    private final int PASSED_TEST_CODE = 1;
    private final int FAILED_TEST_CODE = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_selection);
    }

    public void startTest(View view) {
        int testId = view.getId();
        switch (testId) {
            case R.id.mathTestButton:
                startMathTest();
                break;
            case R.id.sequenceTestButton:
                startSequenceTest();
                break;
            case R.id.speechTestButton:
                startBalanceTest();
                break;
        }

    }

    public void startMathTest() {
        Intent mathTestIntent = new Intent(this, MathTest.class);
        startActivityForResult(mathTestIntent, MATH_TEST_REQ_CODE);
    }

    public void startSequenceTest() {
        Intent sequenceTestIntent = new Intent(this, SequenceTest.class);
        startActivityForResult(sequenceTestIntent, SEQUENCE_TEST_REQ_CODE);
    }

    public void startBalanceTest() {
        Intent balanceTestIntent = new Intent(this, SpeechTest.class);
        startActivityForResult(balanceTestIntent, SPEECH_TEST_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case MATH_TEST_REQ_CODE:
                if (resultCode == PASSED_TEST_CODE) {
                    Log.v("MATH", "SUCCESS");
                } else {
                    Log.v("MATH", "FAIL");
                }
                break;
            case SEQUENCE_TEST_REQ_CODE:
                if (resultCode == PASSED_TEST_CODE) {

                }
                break;
            case SPEECH_TEST_REQ_CODE:
                if (resultCode == PASSED_TEST_CODE) {

                }
                break;
        }
    }
}
