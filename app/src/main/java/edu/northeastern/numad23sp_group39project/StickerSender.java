package edu.northeastern.numad23sp_group39project;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.security.Timestamp;
import java.util.Date;

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

    private DatabaseReference mDatabase;
    private ConstraintLayout messageDisplay;
    private TextView timeText;
    private TextView fromText;
    private ImageView receivedImage;
    private long createTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_sender);
        // add back button in action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        createTime = (new Date()).getTime();

        // add database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        SmileCheckBox = findViewById(R.id.SmileCheckBox);
        AngryCheckBox = findViewById(R.id.AngryCheckBox);
        CryCheckBox = findViewById(R.id.CryCheckBox);
        ReceiverUserName = findViewById(R.id.ReceiverUserName);
        // setup user login
        mTvUsername = findViewById(R.id.LoginUser);
        // show received message
        messageDisplay = findViewById(R.id.messageDisplay);
        fromText = findViewById(R.id.fromText);
        timeText = findViewById(R.id.timeText);
        receivedImage = findViewById(R.id.receivedImage);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        username = sharedPref.getString(getString(R.string.saved_username_key), "");

        mTvUsername.setText("Welcome, " + username + "!");

        // set up notification channel
        createNotificationChannel();

        // restore rotation status
        if (savedInstanceState != null) {
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
                CryCheckBox.setChecked(false);
                AngryCheckBox.setChecked(false);
            }
        });
        CryImageView = findViewById(R.id.CryImageView);
        CryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CryCheckBox.setChecked(!CryCheckBox.isChecked());
                SmileCheckBox.setChecked(false);
                AngryCheckBox.setChecked(false);
            }
        });
        AngryImageView = findViewById(R.id.AngryImageView);
        AngryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AngryCheckBox.setChecked(!AngryCheckBox.isChecked());
                CryCheckBox.setChecked(false);
                SmileCheckBox.setChecked(false);
            }
        });


        SmileCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CryCheckBox.setChecked(false);
                AngryCheckBox.setChecked(false);
            }
        });
        AngryCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CryCheckBox.setChecked(false);
                SmileCheckBox.setChecked(false);
            }
        });
        CryCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AngryCheckBox.setChecked(false);
                SmileCheckBox.setChecked(false);
            }
        });


        // set up listener for buttons
        Button SendButton = findViewById(R.id.SendButton);
        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Write in database
                if (!SmileCheckBox.isChecked() && !AngryCheckBox.isChecked() && !CryCheckBox.isChecked()) {
                    Toast.makeText(getApplicationContext()
                            , "No sticker selected", Toast.LENGTH_SHORT).show();
                }
                if (ReceiverUserName.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext()
                            , "No receiver name entered", Toast.LENGTH_SHORT).show();
                }
                String receiver = ReceiverUserName.getText().toString();
                String sticker = "";
                if (SmileCheckBox.isChecked()) {
                    sticker = "smile.png";
                } else if (AngryCheckBox.isChecked()) {
                    sticker = "angry.png";
                } else if (CryCheckBox.isChecked()) {
                    sticker = "cry.png";
                }
                String time = String.valueOf((new Date()).getTime());
                Message message = new Message(username, receiver, sticker, time);
                Task t = mDatabase.child("messages").child(time).setValue(message);
                Log.d("_____task", t.toString());
                Toast.makeText(getApplicationContext(), "Sticker sent",Toast.LENGTH_SHORT).show();
            }
        });
        Button CountButton = findViewById(R.id.CountButton);
        CountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Read Count data from database
                // use another view to display data
                Intent countIntent = new Intent(getApplicationContext(),StickerCount.class);
                startActivity(countIntent);
            }
        });
        Button HistoryButton = findViewById(R.id.HistoryButton);
        HistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Read History Data from database
                // use another view to display data
                Intent intent = new Intent(StickerSender.this, StickerHistory.class);
                startActivity(intent);
            }
        });

        mDatabase.child("messages").addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        showMessage(dataSnapshot);
                        showNotification(dataSnapshot);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext()
                                , "DBError: " + databaseError, Toast.LENGTH_SHORT).show();
                    }
                }
        );


    }

    private void showMessage(DataSnapshot dataSnapshot) {
        Message message = dataSnapshot.getValue(Message.class);
        if (!message.to.equals(username)) {
            return;
        }
        fromText.setText("From: " + message.from);
        timeText.setText("Time: " + (new Date(Long.parseLong(message.time))).toString());
        if (message.sticker.equals("smile.png")) {
            receivedImage.setImageResource(R.drawable.smile);
        } else if (message.sticker.equals("angry.png")) {
            receivedImage.setImageResource(R.drawable.angry);
        } else {
            receivedImage.setImageResource(R.drawable.cry);
        }
        messageDisplay.setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext()
                , "Received a message", Toast.LENGTH_SHORT).show();
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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("my_channel_id", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(DataSnapshot dataSnapshot) {
        // read data from database
        Message message = dataSnapshot.getValue(Message.class);
        if (!message.to.equals(username)) {
            return;
        }
        String fromUser = message.from;
        if (Long.parseLong(message.time) < createTime) {
            return;
        }
        String sendTime = (new Date(Long.parseLong(message.time))).toString();
        int resId = R.drawable.smile;
        if (message.sticker.equals("smile.png")) {
            resId = R.drawable.smile;
        } else if (message.sticker.equals("angry.png")) {
            resId = R.drawable.angry;
        } else {
            resId = R.drawable.cry;
        }

        // implement notification bar
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_id")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Incoming Sticker Reminder")
                .setContentText("You have received a sticker from " + fromUser + "\n" + "Time: " + sendTime)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), resId))
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(BitmapFactory.decodeResource(getResources(), resId))
                        .bigLargeIcon(null))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(StickerSender.this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS},101);

            return;
        }
        notificationManager.notify(0, builder.build());
    }
}