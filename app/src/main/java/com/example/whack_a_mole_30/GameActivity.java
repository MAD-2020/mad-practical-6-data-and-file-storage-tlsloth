package com.example.whack_a_mole_30;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private String TAG = "Whack-A-Mole 3.0";
    private Integer difficulty;
    private Integer[] settings = {10,9,8,7,6,5,4,3,2,1};
    CountDownTimer getReady;
    CountDownTimer InfiniteLoop;
    TextView score;
    Button back;
    private Integer intscore = 0;
    List<Button> buttonList = new ArrayList<>();
    boolean gamestart = false;
    private DBHandler dbHandler = new DBHandler(this,null,null,1);

    private void readyTimer(){
        /*  HINT:
            The "Get Ready" Timer.
            Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
            Toast message -"Get Ready In X seconds"
            Log.v(TAG, "Ready CountDown Complete!");
            Toast message - "GO!"
            belongs here.
            This timer countdown from 10 seconds to 0 seconds and stops after "GO!" is shown.
         */

        getReady = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long l) {
                Log.v(TAG, "Ready CountDown!" + l/ 1000);
                Toast.makeText(getApplicationContext(),"Get ready in "+ l/1000 + "seconds!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                Log.v(TAG, "Ready CountDown Complete!");
                Toast.makeText(getApplicationContext(),"GO!",Toast.LENGTH_SHORT).show();
                getReady.cancel();
                placeMoleTimer();
                gamestart = true;
            }
        };
        getReady.start();


    }
    private void placeMoleTimer(){
        /* HINT:
           Creates new mole location each second.
           Log.v(TAG, "New Mole Location!");
           setNewMole();
           belongs here.
           This is an infinite countdown timer.
         */
        int difficultytimer = 1000*settings[difficulty];
        InfiniteLoop = new CountDownTimer(1000*settings[difficulty],difficultytimer) {
            @Override
            public void onTick(long l) {
                setNewMole();
            }

            @Override
            public void onFinish() {
                InfiniteLoop.start();
            }

        };
        InfiniteLoop.start();

    }

    private static final int[] BUTTON_IDS = {
            /* HINT:
                Stores the 9 buttons IDs here for those who wishes to use array to create all 9 buttons.
                You may use if you wish to change or remove to suit your codes.*/
            R.id.button,R.id.button2,R.id.button3,R.id.button4,R.id.button5,R.id.button6,R.id.button7,R.id.button8,R.id.button9
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent receiveDifficulty =getIntent();
        difficulty = receiveDifficulty.getIntExtra("level",1);
        Log.v(TAG,"Difficulty:"+difficulty);
        Log.v(TAG,"Mole timer:"+settings[difficulty]);
        readyTimer();
        score = findViewById(R.id.Score);
        for(int id : BUTTON_IDS){
            Button button = findViewById(id);
            buttonList.add(button);
        }
        back = findViewById(R.id.BackButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dbHandler.getScore(GameActivity.this,difficulty+1) < intscore) {
                    dbHandler.saveScore((difficulty + 1), intscore, GameActivity.this);
                }
                //return to select page
                Intent intent = new Intent(GameActivity.this,SelectLevel.class);
                startActivity(intent);

            }
        });


    }

    @Override
    protected void onStart(){
        setNewMole();
        UpdateScore();
        super.onStart();
    }
    private boolean doCheck(Button checkButton)
    {
        /* Hint:
            Checks for hit or miss
            Log.v(TAG, "Hit, score added!");
            Log.v(TAG, "Missed, point deducted!");
            belongs here.
        */
        String text = (String) checkButton.getText();
        return text == "*";
    }

    public void setNewMole()
    {
        // Hint:
        //   Clears the previous mole location and gets a new random location of the next mole location.
        //   Sets the new location of the mole.
        //
        Random ran = new Random();
        ArrayList<Button>moles = new ArrayList<>();
        int randomLocation = ran.nextInt(9);
        Button mole = buttonList.get(randomLocation);
        moles.add(mole);
        if(difficulty > 5) {
            int randomlocation2 = ran.nextInt(9);
            Button mole2 = buttonList.get(randomlocation2);
            moles.add(mole2 );
        }

        for (Button button : buttonList){
            if(moles.contains(button)){
                button.setText("*");
                Log.v(TAG,"New mole location!");
            }
            else{
                button.setText("O");
            }
        }


    }
    public void UpdateScore(){
        String stringscore = String.valueOf(intscore);
        score.setText(stringscore);
    }

    public void onClickButton(View v){
        if (gamestart) {
            Button b = (Button) v;
            if (doCheck(b)) {
                intscore++;
                Log.v(TAG, "Mole hit! Point added!");
                setNewMole();
            } else {
                intscore--;
                Log.v(TAG, "Mole missed, Point deducted.");
            }
            UpdateScore();
        }

    }
}