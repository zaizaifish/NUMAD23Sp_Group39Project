package edu.northeastern.numad23sp_group39project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FinalLoginActivity extends AppCompatActivity {
    private EditText emailText;
    private EditText passwordText;
    private Button btn1; // login button
    private Button btn2; // register button
    private FirebaseAuth mAuth; // Firebase Authentication instance
    private SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_login);
        // add button
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        btn1 = findViewById(R.id.loginbtn);
        btn2 = findViewById(R.id.registerbtn);
        mAuth = FirebaseAuth.getInstance();

        // restore after rotation
        if (savedInstanceState != null) {
            emailText.setText(savedInstanceState.getString("emailText"));
            passwordText.setText(savedInstanceState.getString("passwordText"));
        }

        // log in session
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // User login successful
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(getApplicationContext(), "Log in Success.",
                                            Toast.LENGTH_SHORT).show();
                                    // store user info
                                    String userId = user.getUid();
                                    String userEmail = user.getEmail();
                                    String userDisplayName = user.getDisplayName();
                                    mSharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                                    editor.putString("userId", userId);
                                    editor.putString("userEmail", userEmail);
                                    editor.putString("userDisplayName", userDisplayName);
                                    editor.apply();
                                    Intent intent = new Intent(getApplicationContext(), FinalMainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // User login failed
                                    Toast.makeText(getApplicationContext(), "Log in failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // register session
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // User register successful
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(getApplicationContext(), "Register Success.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // User register failed
                                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("emailText", emailText.getText().toString());
        outState.putString("passwordText", passwordText.getText().toString());
    }

}