package com.example.twmoore.drunktest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            case R.id.balanceTestButton:
                startBalanceTest();
                break;
        }

    }

    public void startMathTest() {
        Intent mathTestIntent = new Intent(this, MathTest.class);
        startActivity(mathTestIntent);
    }

    public void startSequenceTest() {
        Intent sequenceTestIntent = new Intent(this, SequenceTest.class);
        startActivity(sequenceTestIntent);
    }

    public void startBalanceTest() {
        Intent balanceTestIntent = new Intent(this, BalanceTest.class);
        startActivity(balanceTestIntent);
    }
}
