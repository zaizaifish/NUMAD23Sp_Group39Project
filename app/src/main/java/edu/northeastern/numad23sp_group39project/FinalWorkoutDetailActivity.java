package edu.northeastern.numad23sp_group39project;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FinalWorkoutDetailActivity extends AppCompatActivity implements OnMapReadyCallback{

    private TextView workoutType, workoutDate, workoutLength, workoutDistance, heartRate, caloriesBurned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_workout_detail);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        workoutType = findViewById(R.id.workout_type);
        workoutDate = findViewById(R.id.workout_date);
        workoutLength = findViewById(R.id.workout_length);
        workoutDistance = findViewById(R.id.workout_distance);
        heartRate = findViewById(R.id.heart_rate);
        caloriesBurned = findViewById(R.id.calories_burned);

        // Set values for text views from workout data (e.g., from an intent or a database)

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng sanJose = new LatLng(37.3382, -121.8863);
        googleMap.addMarker(new MarkerOptions().position(sanJose).title("Marker in San Jose"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sanJose, 10.0f));
    }
}
