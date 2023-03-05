package edu.northeastern.numad23sp_group39project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class StickerSender extends AppCompatActivity {
    private TextView mTvUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_sender);
        // add back button in action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // setup user login
        Intent LogInintent = new Intent(getApplicationContext(), StickerLoginActivity.class);
        startActivity(LogInintent);
        mTvUsername = findViewById(R.id.tv_username);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String username = sharedPref.getString(getString(R.string.saved_username_key), "");

        mTvUsername.setText("Welcome, " + username + "!");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent backToAPIServiceIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(backToAPIServiceIntent);
        return true;
    }
}