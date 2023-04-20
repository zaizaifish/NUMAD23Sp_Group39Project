package edu.northeastern.numad23sp_group39project;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class FinalWorkoutDetailActivity extends AppCompatActivity {

    private TextView workoutType, workoutDate, workoutLength, workoutDistance, heartRate, caloriesBurned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_workout_detail);

        workoutType = findViewById(R.id.workout_type);
        workoutDate = findViewById(R.id.workout_date);
        workoutLength = findViewById(R.id.workout_length);
        workoutDistance = findViewById(R.id.workout_distance);
        heartRate = findViewById(R.id.heart_rate);
        caloriesBurned = findViewById(R.id.calories_burned);

        // Set values for text views from workout data (e.g., from an intent or a database)

    }
}
