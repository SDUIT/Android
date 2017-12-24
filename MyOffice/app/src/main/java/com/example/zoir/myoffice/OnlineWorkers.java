package com.example.zoir.myoffice;

import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OnlineWorkers extends AppCompatActivity {

    private String mWifi;
    private TextView mWifiName;

    private ListView mWorkers;

    private List<String> online = new ArrayList<String>();

    DatabaseReference mRef;
    FirebaseAuth mAuth;

    SpannableString work;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_workers);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();

        mWifiName = (TextView) findViewById(R.id.wifiName);
        Intent intent = getIntent();
        mWifi = intent.getStringExtra("e1");
        mWifiName.setText("Workers of (" + mWifi + ")");

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users").
                child(mAuth.getCurrentUser().getUid()).child("Office").child("Create").child(mWifi);

        mWorkers = (ListView) findViewById(R.id.Workers);

        ArrayAdapter<String> adapter = new ArrayAdapter< String > (this, android.R.layout.simple_list_item_1, online );
        mWorkers.setAdapter(adapter);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(  DataSnapshot dsp : dataSnapshot.getChildren() ) {
                    if( !dsp.getKey().toString().equals("Names")  ) {

                        Long num = (Long) dsp.child("Time").getValue();
                        Boolean onl = (Boolean) dsp.child("Now").getValue();
                        if( onl ) {
                            work =  new SpannableString("Online");
                        }
                        else {
                            work =  new SpannableString("Ofline");
                        }
                        num = (num-1) * 5;

                        Timestamp timestamp = new Timestamp( (Long) dsp.child("StartStamp").getValue() );
                        Date d = new Date(timestamp.getTime());

                        String cTime = String.format("%02d:%02d:%02d",
                            d.getHours(),
                            d.getMinutes(),
                            d.getSeconds()
                        );

                        online.add(dsp.child("name").getValue() + "\nПриходил в: " + cTime + "\nРаботал: "
                                +String.format("%02d:%02d:00", num/60 , num%60 ) + " час  \nШас:" + work + "\n" );
                    }
                    else
                      online.add( dsp.getKey() ) ;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
