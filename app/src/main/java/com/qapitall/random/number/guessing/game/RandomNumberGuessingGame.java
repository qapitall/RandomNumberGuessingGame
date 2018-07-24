package com.qapitall.random.number.guessing.game;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Qapitall on 24/07/18.
 */

public class RandomNumberGuessingGame extends AppCompatActivity {

    LinearLayout numbersLine;
    Button button;
    SeekBar seekBar;
    TextView howToRestartText;

    int previousProgress = 0;
    int lengthOfRandomNumber;
    ArrayList<Integer> randomNumbers = new ArrayList<>();

    // We have identified the required components

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_number_guessing_game);

        numbersLine = findViewById(R.id.numbersLine);
        button = findViewById(R.id.button);
        seekBar = findViewById(R.id.seekBar);
        howToRestartText = findViewById(R.id.restartGameInfo);
        howToRestartText.setVisibility(View.INVISIBLE);

        for(int k=0 ; k < 3 ; k++){
            addTextView(k); // We add 3 TextViews(default) to NumbersLine
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                int progress = seekBar.getProgress();

                if (previousProgress < progress) {

                    /*
                     * We checked whether the new textView will be added or deleted
                     * If progress is bigger than previousProgress we add new textView
                     * else we delete the last textView
                     *
                    */

                    switch (progress) {
                        case 1:  //  (Normal level)

                            addTextView(3);
                            break;

                        case 2:  //  (Difficult level)

                            if(previousProgress ==0) // It works if it is pulled from Easy level to Difficult level
                                addTextView(3);

                            addTextView(4);
                            break;
                    }

                }
                else {

                    switch (previousProgress) {
                        case 1:  //  (Normal level)

                            deleteTextView(3);
                            break;

                        case 2:  //  (Difficult level)

                            if(progress==0) //It works if it is pulled from Difficult level to Easy level
                                deleteTextView(3);

                            deleteTextView(4);
                            break;
                    }

                }

                previousProgress = progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                howToRestartText.setVisibility(View.INVISIBLE);
                button.setClickable(true);
                button.setVisibility(View.VISIBLE);

                // We prepared for the new game
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                lengthOfRandomNumber = seekBar.getProgress()+3;
                Random rand = new Random();
                randomNumbers.clear();

                for(int i=0 ; i < numbersLine.getChildCount() ;i++){

                    randomNumbers.add(rand.nextInt(10)); // We generate random numbers according to difficulty level and  we added them to the randomNumbers list

                    final TextView textView;
                    textView=findViewById(i);
                    textView.setTextColor(Color.BLACK);
                    textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int textViewInteger = Integer.parseInt(textView.getText().toString());
                        if(textViewInteger<9)  // If the number on the top of textView is 9, it will be 0 at the next click
                            textView.setText(""+(textViewInteger+1));
                        else
                            textView.setText("0");

                    }
                }); // We increased the number on the top of TextView when it is clicked
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView;
                int numberOfCorrectKnownDigits = 0;

                for(int i = 0; i < lengthOfRandomNumber; i++){

                textView = findViewById(i);
                if(randomNumbers.get(i)==Integer.parseInt(textView.getText().toString())) { //We compared the number in textViews with values of the randomNumbers list
                    textView.setTextColor(Color.GREEN);
                    textView.setClickable(false);
                    numberOfCorrectKnownDigits++;
                }
                else
                    textView.setTextColor(Color.RED);

                }

                if(numberOfCorrectKnownDigits == lengthOfRandomNumber){ // If the number is correct we have finished the game

                    howToRestartText.setVisibility(View.VISIBLE);
                    button.setClickable(false);
                    button.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Well Done !",Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public void addTextView(int textViewId){

        TextView textView = new TextView(getApplicationContext());
        textView.setText("" + 0);
        textView.setId(textViewId);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(3, 40);
        numbersLine.addView(textView);

        // We added a new textView to NumbersLine and specified its properties

    }

    public void deleteTextView(int textViewId){

        TextView textView;
        textView = findViewById(textViewId);
        numbersLine.removeView(textView);

        // We delete textView

    }
}
