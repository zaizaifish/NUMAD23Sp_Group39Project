package edu.northeastern.numad23sp_group39project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button mainbtn1; // About Me Button
    private Button mainbtn2; // A7 Button
    private Button mainbtn3; // A8 Button
    private Button mainbtn4; // Final Project Button
    private Button a8Button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainbtn1 = findViewById(R.id.mainbutton1);
        mainbtn2 = findViewById(R.id.mainbutton2);
        mainbtn3 = findViewById(R.id.mainbutton3);
        mainbtn4 = findViewById(R.id.mainbutton4);
        a8Button = findViewById(R.id.a8Button);
        mainbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AboutMe.class);
                startActivity(intent);
            }
        });
        mainbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), APIService.class);
                startActivity(intent);
            }
        });
        a8Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), StickerSender.class);
                Intent intent = new Intent(getApplicationContext(), StickerLoginActivity.class);
                startActivity(intent);
            }
        });
    }
}