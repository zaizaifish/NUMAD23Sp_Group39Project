package edu.northeastern.numad23sp_group39project;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WorkoutTimerActivity extends AppCompatActivity {

    private EditText editTextMinutes;
    private TextView timerText;
    private Button pauseContinueButton;
    private CountDownTimer countDownTimer;
    private boolean isPaused = true;
    private long remainingTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_timer);

        editTextMinutes = findViewById(R.id.editTextMinutes);
        timerText = findViewById(R.id.timerText);
        pauseContinueButton = findViewById(R.id.pauseContinueButton);

        pauseContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPaused) {
                    if (countDownTimer == null) {
                        String input = editTextMinutes.getText().toString();
                        if (input.isEmpty()) {
                            Toast.makeText(WorkoutTimerActivity.this, "Please enter minutes.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        long minutes = Long.parseLong(input);
                        remainingTime = minutes * 60 * 1000;
                        editTextMinutes.setEnabled(false); // Disable the EditText
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
                editTextMinutes.setEnabled(true); // Re-enable the EditText
                isPaused = true;
                countDownTimer = null;
            }
        };

        countDownTimer.start();
        pauseContinueButton.setText("Pause");
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
}