package edu.northeastern.numad23sp_group39project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextClock;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StickerCount extends AppCompatActivity {
    private TextView countSmileView;

    private TextView countAngryView;

    private TextView countCryView;

    private DatabaseReference databaseReference;

    private Integer countSmile = 0;

    private Integer countAngry = 0;

    private Integer countCry = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_count);
        countSmileView = findViewById(R.id.countSmile);
        countAngryView = findViewById(R.id.countAngry);
        countCryView = findViewById(R.id.countCry);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String usrname = sharedPref.getString(getString(R.string.saved_username_key), "");
        if (savedInstanceState!=null){
            countSmileView.setText(savedInstanceState.getString("smileCount"));
            countAngryView.setText(savedInstanceState.getString("angryCount"));
            countCryView.setText(savedInstanceState.getString("cryCount"));
        }else{
            Query countQuery = databaseReference.child("messages").orderByChild("from").equalTo(usrname);
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        String sticker = ds.child("sticker").getValue(String.class);
                        switch(sticker){
                            case "smile.png":
                                countSmile++;
                                break;
                            case "angry.png":
                                countAngry++;
                                break;
                            case "cry.png":
                                countCry++;
                                break;
                            default:
                                break;
                        }
                    }
                    countSmileView.setText(String.valueOf(countSmile));
                    countAngryView.setText(String.valueOf(countAngry));
                    countCryView.setText(String.valueOf(countCry));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("TAG", databaseError.getMessage()); //Don't ignore potential errors!
                }
            };
            countQuery.addListenerForSingleValueEvent(valueEventListener);
        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("smileCount", countSmileView.getText().toString());
        outState.putString("angryCount", countAngryView.getText().toString());
        outState.putString("cryCount", countCryView.getText().toString());

    }
}