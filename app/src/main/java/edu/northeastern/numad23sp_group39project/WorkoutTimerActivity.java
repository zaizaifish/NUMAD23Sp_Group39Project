package edu.northeastern.numad23sp_group39project;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

public class WorkoutTimerActivity extends AppCompatActivity {

    private LinearLayout timePickerLayout;
    private NumberPicker hourPicker, minutePicker, secondPicker;
    private TextView timerText;
    private Button pauseContinueButton;
    private CountDownTimer countDownTimer;
    private Button stopButton;
    private boolean isPaused = true;
    private long remainingTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_timer);

        timePickerLayout = findViewById(R.id.timePickerLayout);
        hourPicker = findViewById(R.id.hourPicker);
        minutePicker = findViewById(R.id.minutePicker);
        secondPicker = findViewById(R.id.secondPicker);
        stopButton = findViewById(R.id.stopButton);

        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        secondPicker.setMinValue(0);
        secondPicker.setMaxValue(59);

        timerText = findViewById(R.id.timerText);
        pauseContinueButton = findViewById(R.id.pauseContinueButton);


        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
            }
        });

        pauseContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPaused) {
                    if (countDownTimer == null) {
                        int hours = hourPicker.getValue();
                        int minutes = minutePicker.getValue();
                        int seconds = secondPicker.getValue();
                        if (hours == 0 && minutes == 0 && seconds == 0) {
                            Toast.makeText(WorkoutTimerActivity.this, "Please set a valid time.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        remainingTime = (hours * 60 * 60 + minutes * 60 + seconds) * 1000;
                        timePickerLayout.setVisibility(View.GONE); // Hide the time pickers
                    }
                    startTimer(remainingTime);
                } else {
                    pauseTimer();
                }
            }
        });
    }


    private void startTimer(long millisInFuture) {
        countDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
                updateTimerText(remainingTime);
            }

            @Override
            public void onFinish() {
                timerText.setText("00:00:00");
                Toast.makeText(WorkoutTimerActivity.this, "Time is up!", Toast.LENGTH_SHORT).show();
                pauseContinueButton.setText("Start");
                timePickerLayout.setVisibility(View.VISIBLE); // Show the time pickers
                isPaused = true;
                countDownTimer = null;
            }
        };

        countDownTimer.start();
        pauseContinueButton.setText("Pause");
        stopButton.setVisibility(View.VISIBLE); // Show the "Stop" button
        isPaused = false;
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            pauseContinueButton.setText("Continue");
            isPaused = true;
        }
    }

    private void updateTimerText(long millis) {
        int hours = (int) (millis / (1000 * 60 * 60));
        int minutes = (int) (millis % (1000 * 60 * 60) / (1000 * 60));
        int seconds = (int) (millis % (1000 * 60) / 1000);
        String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerText.setText(time);
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            timerText.setText("00:00:00");
            pauseContinueButton.setText("Start");
            stopButton.setVisibility(View.GONE);
            timePickerLayout.setVisibility(View.VISIBLE);
            isPaused = true;
            countDownTimer = null;
        }
    }
}