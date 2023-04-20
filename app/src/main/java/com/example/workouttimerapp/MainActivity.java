
/*
            Name        :  Surpreet Singh
            Student ID  :  218663803
            Unit No.    :  SIT305

 */


package com.example.workouttimerapp;

//importing libraries
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {



    //  Declaring variables for the widgets
    private TextView textViewWorkoutTimeUpdate;
    private TextView textViewRestTimeUpdate;
    private TextView textViewRemainingSetsUpdate;
    private EditText WorkoutTimeUserEntered;
    private EditText RestTimeUserEntered;
    private EditText noOfSetsEditText;
    private Button startButton;
    private Button stopButton;
    private ProgressBar progressBar;

    private CountDownTimer workoutTimer;
    private CountDownTimer restTimer;
    private int workoutDuration = 10;
    private int restDuration = 5;
    private int noOfSets;
    int setsCounter= 1;
    MediaPlayer mediaPlayer;

    //declaring boolean variables to check whether workout and rest timers are running or not, and an integer to hold progress status.
    private boolean isWorkoutTimerRunning = false;
    private boolean isRestTimerRunning = false;
    int progressStatus = 0;

    //Override function for the onCreate method and its called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Referencing to the XML file UI elements
        noOfSetsEditText=findViewById(R.id.noOfSets);
        WorkoutTimeUserEntered = findViewById(R.id.workout_duration);
        RestTimeUserEntered = findViewById(R.id.rest_duration);
        textViewRemainingSetsUpdate = findViewById(R.id.textView4);
        startButton = findViewById(R.id.start_button);
        stopButton = findViewById(R.id.stop_button);
        progressBar = findViewById(R.id.progress_bar);
        textViewWorkoutTimeUpdate = findViewById(R.id.textView2);
        textViewRestTimeUpdate = findViewById(R.id.textView3);
        //Try-Catch to tackle any runtime error.
        try {

            //onClickListener for the startButton
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //checking if the workout and rest timers are not running.
                    if (!isWorkoutTimerRunning && !isRestTimerRunning) {
                        progressStatus = 0; //setting progress status back to 0
                        // getting the values which user has put in
                        workoutDuration = Integer.parseInt(WorkoutTimeUserEntered.getText().toString());
                        restDuration = Integer.parseInt(RestTimeUserEntered.getText().toString());
                        noOfSets = Integer.parseInt(noOfSetsEditText.getText().toString());
                        textViewRemainingSetsUpdate.setText(Integer.toString(noOfSets)); //setting the value to textbox
                        //checking if the number of sets is not zero.
                        if(noOfSets!=0){
                            startWorkoutTimer(); //starting the workout timer by calling the method
                            startButton.setEnabled(false);  //disabling the start button

                        }

                    }
                }
            });

            //On click listener for the stop button.
            stopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //checking if the workout timer is running.
                    if (isWorkoutTimerRunning) {
                        workoutTimer.cancel(); //Stopping the workout timer.
                        isWorkoutTimerRunning = false;  //setting the isWorkoutTimerRunning variable to false
                    }
                    //checking if the rest timer is running.
                    if (isRestTimerRunning) {
                        restTimer.cancel();  //Stopping the Rest timer.
                        isRestTimerRunning = false; //setting the isRestTimerRunning variable to false
                    }

                    //stopping the media player if it is playing.
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                    startButton.setEnabled(true); //Enabling back the start button

                }
            });

        }catch (RuntimeException e){

            Log.v("Error","Runtime Error"); //logging the error if there will be any runtime error

        }


    }

    // Defining a method to start the workout timer.
    private void startWorkoutTimer() {

        // Creating a new CountDownTimer with the given duration and interval.
        workoutTimer = new CountDownTimer(workoutDuration * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                isWorkoutTimerRunning = true; //setting the isWorkoutTimerRunning variable to true
                {

                    // Updating the workout time remaining in the UI.
                    textViewWorkoutTimeUpdate.setText(String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                    // Setting the progress tint color and updating the progress bar.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        progressBar.setProgressTintList(ColorStateList.valueOf( 0XFFBB86FC));
                    }
                    textViewRestTimeUpdate.setBackgroundColor(0XFFFFFFFF); //changing the background color of the text box
                    textViewWorkoutTimeUpdate.setBackgroundColor(0XFFBB86FC);  //changing the background color of the text box
                    progressStatus += 100 / workoutDuration;  //updating the progress status
                    progressBar.setProgress(progressStatus);  //updating the progress of the progressbar

                }

            }

            @Override
            public void onFinish() {
                isWorkoutTimerRunning = false;
                progressStatus = 0; //setting the progress status back to 0
                startRestTimer();  //calling the startRestTimer() method to start the rest timer

            }
        }.start();
    }


    // Defining a method to start the rest timer.
    private void startRestTimer() {
        // Creating a new CountDownTimer with the given duration and interval.
        restTimer = new CountDownTimer(restDuration * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                isRestTimerRunning = true;
                // Updating the rest time remaining in the UI.
                textViewRestTimeUpdate.setText(String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                // Setting the progress tint color and updating the progress bar.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    progressBar.setProgressTintList(ColorStateList.valueOf(0XFFF96F6F));
                }
                textViewRestTimeUpdate.setBackgroundColor(0XFFF96F6F); //changing the background color of the text box
                textViewWorkoutTimeUpdate.setBackgroundColor(0XFFFFFFFF); //changing the background color of the text box
                progressStatus += 100 / restDuration; //updating the progress status
                progressBar.setProgress(progressStatus);  //updating the progress of the progressbar
            }

            @Override
            public void onFinish() {
                isRestTimerRunning = false;
                // Checking if there are more sets to be completed.
                if(setsCounter<=noOfSets){
                    setsCounter++;  //incrementing the sets counter
                    progressStatus = 0;

                    startWorkoutTimer(); //calling a startWorkoutTimer() method to start the workout timer again

                }else{
                    // If all sets are completed, making a sound.
                    makeSound();
                }
                textViewRemainingSetsUpdate.setText(Integer.toString(--noOfSets));  //updating the no of sets remaining in the text box


            }
        }.start();
    }

    // Defining a method to play a sound.
    private void makeSound() {
        // Creating a new MediaPlayer with the default alarm alert sound and playing it.
        mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI);
        mediaPlayer.start();

    }

}