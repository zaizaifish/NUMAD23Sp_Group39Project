package edu.northeastern.numad23sp_group39project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StickerLoginActivity extends AppCompatActivity {
    private EditText mEditUsername;
    private Button mBtnSubmit;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_login);

        mEditUsername = findViewById(R.id.edit_username);
        mBtnSubmit = findViewById(R.id.btn_submit);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mEditUsername.getText().toString();

                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.saved_username_key), username);
                editor.apply();
                Intent LogInIntent = new Intent(getApplicationContext(), StickerSender.class);

                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users");

                Task t = mDatabase.child("users").child(username).setValue(username);

                startActivity(LogInIntent);
                finish();
            }
        });

    }
}