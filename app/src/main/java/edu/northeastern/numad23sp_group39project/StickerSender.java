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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class StickerSender extends AppCompatActivity {
    private TextView mTvUsername;
    private String username;
    private EditText ReceiverUserName;
    private ImageView SmileImageView;
    private ImageView CryImageView;
    private ImageView AngryImageView;
    private CheckBox SmileCheckBox;
    private CheckBox AngryCheckBox;
    private CheckBox CryCheckBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_sender);
        // add back button in action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        SmileCheckBox = findViewById(R.id.SmileCheckBox);
        AngryCheckBox = findViewById(R.id.AngryCheckBox);
        CryCheckBox = findViewById(R.id.CryCheckBox);
        ReceiverUserName = findViewById(R.id.ReceiverUserName);
        // setup user login
        mTvUsername = findViewById(R.id.LoginUser);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        username = sharedPref.getString(getString(R.string.saved_username_key), "");

        mTvUsername.setText("Welcome, " + username + "!");

        // restore rotation status
        if(savedInstanceState != null) {
            boolean isCheckBoxChecked = savedInstanceState.getBoolean("isSmileBoxChecked");
            SmileCheckBox.setChecked(isCheckBoxChecked);
            isCheckBoxChecked = savedInstanceState.getBoolean("isAngryBoxChecked");
            AngryCheckBox.setChecked(isCheckBoxChecked);
            isCheckBoxChecked = savedInstanceState.getBoolean("isCryBoxChecked");
            CryCheckBox = findViewById(R.id.CryCheckBox);
            String savedText = savedInstanceState.getString("ReceiverUserName");
            ReceiverUserName.setText(savedText);
        }
        // setup sticker listener
        SmileImageView = findViewById(R.id.SmileImageView);
        SmileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmileCheckBox.setChecked(!SmileCheckBox.isChecked());
            }
        });
        CryImageView = findViewById(R.id.CryImageView);
        CryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CryCheckBox.setChecked(!CryCheckBox.isChecked());
            }
        });
        AngryImageView = findViewById(R.id.AngryImageView);
        AngryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AngryCheckBox.setChecked(!AngryCheckBox.isChecked());
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("ReceiverUserName", ReceiverUserName.getText().toString());
        outState.putBoolean("isSmileBoxChecked", SmileCheckBox.isChecked());
        outState.putBoolean("isAngryBoxChecked", AngryCheckBox.isChecked());
        outState.putBoolean("isCryBoxChecked", CryCheckBox.isChecked());
    }
}