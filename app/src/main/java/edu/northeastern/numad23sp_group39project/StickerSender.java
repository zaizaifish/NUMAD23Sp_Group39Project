package edu.northeastern.numad23sp_group39project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StickerSender extends AppCompatActivity {
    private TextView mTvUsername;
    private String username;
    private TextView SendToTextView;
    private ImageView SmileImageView;
    private int SmileCnt;
    private ImageView CryImageView;
    private int CryCnt;
    private ImageView AngryImageView;
    private int AngryCnt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_sender);
        // add back button in action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // setup user login
        mTvUsername = findViewById(R.id.LoginUser);
        SendToTextView = findViewById(R.id.SendToTextView);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        username = sharedPref.getString(getString(R.string.saved_username_key), "");

        mTvUsername.setText("Welcome, " + username + "!");

        // setup sticker listener
        SmileImageView = findViewById(R.id.SmileImageView);
        SmileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmileCnt++;
                TextView SmileCountTextView = findViewById(R.id.SmileCountTextView);
                SmileCountTextView.setText(String.valueOf(SmileCnt));
            }
        });
        CryImageView = findViewById(R.id.CryImageView);
        CryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CryCnt++;
                TextView CryCountTextView = findViewById(R.id.CryCountTextView);
                CryCountTextView.setText(String.valueOf(CryCnt));
            }
        });
        AngryImageView = findViewById(R.id.AngryImageView);
        AngryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AngryCnt++;
                TextView AngryCountTextView = findViewById(R.id.AngryCountTextView);
                AngryCountTextView.setText(String.valueOf(AngryCnt));
            }
        });

        // set up listener for buttons
        Button SendButton = findViewById(R.id.SendButton);
        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Write in database

            }
        });
        Button CountButton = findViewById(R.id.CountButton);
        CountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Read Count data from database
                // use another view to display data
            }
        });
        Button HistoryButton = findViewById(R.id.HistoryButton);
        HistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Read History Data from database
                // use another view to display data
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent backToAPIServiceIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(backToAPIServiceIntent);
        return true;
    }
}