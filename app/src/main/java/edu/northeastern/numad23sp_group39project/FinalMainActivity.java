package edu.northeastern.numad23sp_group39project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

public class FinalMainActivity extends AppCompatActivity {
    private TextView username;
    private Button btn1; // daily exercise button
    private Button btn2; // exercise list button
    private Button btn3; // report button
    private Button btn4; // log in button
    private FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_main);

        username = findViewById(R.id.username);
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);
        // retrieve user status
        SharedPreferences mSharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userId = mSharedPreferences.getString("userId", null);
        String userEmail = mSharedPreferences.getString("userEmail", null);
        String userDisplayName = mSharedPreferences.getString("userDisplayName", null);

        if (userId != null && userEmail != null) {
           username.setText("User: " + userEmail);
        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WorkoutPlanActivity.class);
                startActivity(intent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FinalWorkoutDetailActivity.class);
                startActivity(intent);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FinalLoginActivity.class);
                startActivity(intent);
            }
        });
    }
}