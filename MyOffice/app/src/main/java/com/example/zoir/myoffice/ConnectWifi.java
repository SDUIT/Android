package com.example.zoir.myoffice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.Console;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.content.Context.WIFI_SERVICE;

public class ConnectWifi extends AppCompatActivity {

    private TextView mWifi;
    private TextView mMessage;
    private EditText mPasswordField;
    private Button mSignBtn;

    FirebaseAuth mAuth;
    DatabaseReference mDatabse;

    private ProgressDialog mProgress;

    private String mSsid, name, ClientId, UserId;

    List<String> mID = new ArrayList<String>();

    WifiManager wifiManager;

    private Long threadNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_wifi);

        threadNumber = new Long(0);

        mAuth = FirebaseAuth.getInstance();
        mDatabse = FirebaseDatabase.getInstance().getReference().child("Users");

        Intent intent = getIntent();
        mSsid = intent.getStringExtra("e1");
        mMessage = (TextView) findViewById(R.id.message);

        mWifi = (TextView) findViewById(R.id.wifiText);
        mWifi.setText( mSsid );
        mPasswordField = (EditText) findViewById(R.id.passwordField);
        mSignBtn = (Button) findViewById(R.id.signBtn);

        mSignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Connect();
            }
        });
    }

    private void Connect() {
         String password = mPasswordField.getText().toString();
         if(!TextUtils.isEmpty(password)) {
             start();
         }
         else {
             Toast.makeText(getApplicationContext() , "Fill password please" , Toast.LENGTH_SHORT).show();
         }
    }

    private void start() {
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", mSsid);
        wifiConfig.preSharedKey = String.format("\"%s\"", mPasswordField.getText().toString());

        wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        if( !wifiManager.isWifiEnabled() ) {
            wifiManager.setWifiEnabled(true);
        }

        int netId = wifiManager.addNetwork(wifiConfig);
        wifiManager.disconnect();
        boolean connect = wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
        if(connect) {

            Toast.makeText(getApplicationContext(),"You connected succesfully",Toast.LENGTH_LONG).show();
            addTofirebase();
            startActivity(new Intent( ConnectWifi.this , MainActivity.class));
        }
        else {
            Toast.makeText(getApplicationContext(),"Incorect password",Toast.LENGTH_SHORT).show();
        }
    }

    private void addTofirebase() {

        mDatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                name = dataSnapshot.child( mAuth.getCurrentUser().getUid() ).child("name").getValue().toString();

                for( final DataSnapshot mData : dataSnapshot.getChildren() ) {

                    DatabaseReference addToDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mData.getKey());

                    if( mData.child("Office").child("Create").hasChild(mSsid) ) {

                         if( mData.child("Office").child("Connect").hasChild( mAuth.getCurrentUser().getUid() ) ) {

                             if ( mData.child("Office").child("Connect").child( mAuth.getCurrentUser().getUid() ).hasChild(mSsid) ) {
                                // Here we shoud write some algorithm wich each 5 minute shoud check if user connected with that wifi, then we shoud sent message to fire base

                                 ClientId = mAuth.getCurrentUser().getUid();
                                 UserId = mData.getKey();

                                 if( mData.child("Office").child("Create").child(mSsid).hasChild(ClientId) ) {
                                       if(  mData.child("Office").child("Create").child(mSsid).child(ClientId).hasChild("Now")  ) {

                                           Boolean use = (Boolean) mData.child("Office").child("Create").child(mSsid).child(ClientId).child("Now").getValue();
                                           //System.out.println("************************* is ==  " + use);

                                           if (use != null && use == false) {

                                               Timestamp pastime = new Timestamp( (Long) mData.child("Office").child("Create")
                                                       .child(mSsid).child(ClientId).child("StartStamp").getValue()  );

                                               Date d1 = new Date( pastime.getTime() );

                                               int dd1 = d1.getDate();

                                               Calendar cal  = Calendar.getInstance();
                                               int day = cal.get( Calendar.DAY_OF_MONTH );

                                               if( dd1 != day ) {
                                                   addToDatabase.child("Office").child("Create").child(mSsid).child(ClientId).removeValue();
                                               }

                                               threadNumber++;
                                               MyRunnable r = new MyRunnable(name, mSsid, UserId, ClientId, mData, threadNumber);
                                               new Thread(r).start();
                                           }
                                       }

                                 }
                                 else {
                                     threadNumber++;
                                     MyRunnable r = new MyRunnable(name, mSsid, UserId, ClientId, mData, threadNumber);
                                     new Thread(r).start();
                                 }
                             }
                             else {
                                // send message to gmail for request;
                                addToDatabase.child("Office").child("Connect").child( mAuth.getCurrentUser().getUid() ).child(mSsid).setValue("connect");
                             }
                         }
                         else {
                             // send message to gmail for request;
                             addToDatabase.child("Office").child("Connect").child( mAuth.getCurrentUser().getUid() ).child(mSsid).setValue("connect");
                         }
                    }
                    else {
                         // It means this user have no wifi such wich you want to connect;
                         // Here we just pass and we don't do anything with it;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    class MyRunnable implements Runnable {

        final AtomicBoolean terminate = new AtomicBoolean(false);
        private String name;
        private String wifiName;
        private String UserId;
        private String ClientId;
        private Long myId;

        DataSnapshot mData;

        WifiManager wifiManager;

        String wifi;

        DatabaseReference mRef;

        Long time;

        MyRunnable(String name, String WifiName, String UserId, String ClientId, DataSnapshot m, Long ii ) {
            this.name = name;
            this.wifiName = WifiName;
            this.UserId = UserId;
            this.ClientId = ClientId;
            this.mData = m;
            this.myId = ii;
            wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

            time = new Long(0) ;

            mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(UserId);

            if (!mData.child("Office").child("Create").child(wifiName).hasChild(ClientId)) {
                add();
            }
            else {

                time = (Long) mData.child("Office").child("Create").child(wifiName).child(ClientId).child("Time").getValue();

            }
        }

        void add() {

                mRef.child("Office").child("Create").child(wifiName).child(ClientId).child("name").setValue(name);
                mRef.child("Office").child("Create").child(wifiName).child(ClientId).child("Time").setValue(time);
                mRef.child("Office").child("Create").child(wifiName).child(ClientId).child("Now").setValue(true);
                mRef.child("Office").child("Create").child(wifiName).child(ClientId).child("StartStamp").setValue(ServerValue.TIMESTAMP);

        }

        public void run() {
            //System.out.println( "Thrad number " + myId + " Started working @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"  );
            while( !terminate.get() ) {

                //System.out.println( "Thread number " + myId + " working--------- Terminate= " + !terminate.get() + "-----------------");

                wifi = wifiManager.getConnectionInfo().getSSID();
                wifi = wifi.substring(1,wifi.length()-1);

                if( wifi.equals( wifiName )  ) {

                    time+=1;
                    mRef.child("Office").child("Create").child(wifiName).child(ClientId).child("Time").setValue( time );
                    mRef.child("Office").child("Create").child(wifiName).child(ClientId).child("Now").setValue(true);

                }
                else {

                    mRef.child("Office").child("Create").child(wifiName).child(ClientId).child("Now").setValue( false );
                    terminate.set(true);

                }

                try {
                    Thread.sleep( 15 * 1000);
                }
                catch (InterruptedException e) {
                    ;
                }

            }
            //System.out.println(" Thread number " + myId + "Stoped ///////////////////////////////////////////////////////////////" );
        }
    }

}
