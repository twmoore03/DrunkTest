package com.example.twmoore.drunktest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by twmoore on 4/27/2017.
 */

public class MathTest extends AppCompatActivity {

    private TextView firstNumber;
    private TextView secondNumber;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.math_test);

        firstNumber = (TextView) findViewById(R.id.firstNumber);
        secondNumber = (TextView) findViewById(R.id.secondNumber);

        generateRandomMathTest();
    }

    private void generateRandomMathTest() {
        int firstRandomNumber = getRandomNumber(1, 50);
        firstNumber.setText(Integer.toString(firstRandomNumber));

        int secondRandomNumber = getRandomNumber(1, 50);
        secondNumber.setText(Integer.toString(secondRandomNumber));
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

            int answer = calculateCorrectAnswer();

            if (numberInput == answer) {
                Toast successToast =  Toast.makeText(getApplicationContext(), "You passed 3rd grade!", Toast.LENGTH_LONG);
                successToast.show();
                userAnswer.clear();
                generateRandomMathTest();
            } else {
                Toast failToast = Toast.makeText(getApplicationContext(), "You're Drunk!!", Toast.LENGTH_LONG);
                failToast.show();
            }
        }
    }

    private int calculateCorrectAnswer() {
        TextView firstNumberView = (TextView) findViewById(R.id.firstNumber);
        TextView secondNumberView = (TextView) findViewById(R.id.secondNumber);

        int firstNumber = Integer.valueOf(firstNumberView.getText().toString());
        int secondNumber = Integer.valueOf(secondNumberView.getText().toString());

        return firstNumber + secondNumber;
    }
}
