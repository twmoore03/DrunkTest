package com.example.twmoore.drunktest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TestSelection extends AppCompatActivity {

    private Button mathTestButton;
    private Button sequenceTestButton;
    private Button speechTestButton;
    private Context context = TestSelection.this;

    private TextView testsPassedTextView;
    private int testsPassedCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_selection);

        mathTestButton = (Button) findViewById(R.id.mathTestButton);
        sequenceTestButton = (Button) findViewById(R.id.sequenceTestButton);
        speechTestButton = (Button) findViewById(R.id.speechTestButton);
        testsPassedTextView = (TextView) findViewById(R.id.testResultsView);

        resetTests();

        updateTestsPassedView();
    }

    private void resetTests() {
        testsPassedCount = 0;

        mathTestButton.setBackgroundColor(getResources().getColor(R.color.darkGray));
        mathTestButton.setEnabled(true);

        sequenceTestButton.setBackgroundColor(getResources().getColor(R.color.darkGray));
        sequenceTestButton.setEnabled(true);

        speechTestButton.setBackgroundColor(getResources().getColor(R.color.darkGray));
        speechTestButton.setEnabled(true);
    }

    private void updateTestsPassedView() {
        String testsPassedString = testsPassedCount + "/3 Passed";
        testsPassedTextView.setText(testsPassedString);
    }

    private void incrementTestsPassedCount() {
        testsPassedCount++;
        if (testsPassedCount == 3) {
            setResult(Constants.SUCCESS_RESULT);
            finish();
        }
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

    private void startMathTest() {
        Intent mathTestIntent = new Intent(this, MathTest.class);
        startActivityForResult(mathTestIntent, Constants.MATH_TEST_REQ_CODE);
    }

    private void startSequenceTest() {
        Intent sequenceTestIntent = new Intent(this, SequenceTest.class);
        startActivityForResult(sequenceTestIntent, Constants.SEQUENCE_TEST_REQ_CODE);
    }

    private void startBalanceTest() {
        Intent balanceTestIntent = new Intent(this, SpeechTest.class);
        startActivityForResult(balanceTestIntent, Constants.SPEECH_TEST_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case Constants.MATH_TEST_REQ_CODE:
                if (resultCode == Constants.PASSED_TEST_CODE) {
                    Log.v("MATH", "SUCCESS");
                    incrementTestsPassedCount();
                    updateTestsPassedView();
                    disableTest(Constants.MATH_TEST_REQ_CODE);
                } else {
                    Log.v("MATH", "FAIL");
                    setResult(Constants.FAILED_TEST_CODE);
                    finish();
                }
                break;
            case Constants.SEQUENCE_TEST_REQ_CODE:
                if (resultCode == Constants.PASSED_TEST_CODE) {
                    Log.v("SEQ","SUCCESS");
                    incrementTestsPassedCount();
                    updateTestsPassedView();
                    disableTest(Constants.SEQUENCE_TEST_REQ_CODE);
                } else {
                    Log.v("SEQ", "FAIL");
                    setResult(Constants.FAILED_TEST_CODE);
                    finish();
                }
                break;
            case Constants.SPEECH_TEST_REQ_CODE:
                if (resultCode == Constants.PASSED_TEST_CODE) {
                    Log.v("SPEECH","SUCCESS");
                    incrementTestsPassedCount();
                    updateTestsPassedView();
                    disableTest(Constants.SPEECH_TEST_REQ_CODE);
                } else {
                    Log.v("SPEECH", "FAIL");
                    setResult(Constants.FAILED_TEST_CODE);
                    finish();
                }
                break;
        }
    }

    private void disableTest(int testCode) {
        switch(testCode) {
            case Constants.MATH_TEST_REQ_CODE:
                mathTestButton.setBackgroundColor(Color.GREEN);
                mathTestButton.setEnabled(false);
                break;
            case Constants.SEQUENCE_TEST_REQ_CODE:
                sequenceTestButton.setBackgroundColor(Color.GREEN);
                sequenceTestButton.setEnabled(false);
                break;
            case Constants.SPEECH_TEST_REQ_CODE:
                speechTestButton.setBackgroundColor(Color.GREEN);
                speechTestButton.setEnabled(false);
                break;
        }
    }

    // Prevents you from going back (Set a timer for this function, could nest functions)
    @Override
    public void onBackPressed() {
        Toast toast = Toast.makeText(context, "You're not allowed to go back!", Toast.LENGTH_SHORT);
        toast.show();
    }
}
