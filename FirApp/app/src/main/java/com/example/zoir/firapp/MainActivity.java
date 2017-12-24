package com.example.zoir.firapp;

import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    /*
    private EditText mUserName;
    private EditText mUserPassword;
    private Button mSendData;
    private TextView mTextdata;
    */

    ArrayList<String> mUsername = new ArrayList<>();
    ListView mListView;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");

        mListView = (ListView) findViewById(R.id.listView);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, mUsername );
        mListView.setAdapter(arrayAdapter);



        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class);
                mUsername.add(value);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*
        mUserName = (EditText) findViewById(R.id.userName);
        mUserPassword = (EditText) findViewById(R.id.userPassword);
        mSendData = (Button) findViewById(R.id.sendData);
        mTextdata = (TextView) findViewById(R.id.textData);
       */

        //Changin on Real time;
        /*
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Map<String, String> value = dataSnapshot.getValue(Map.class);
                    Log.v("E_VALUE", "value :" + value.get("name") );

                }
                catch (DatabaseException e) {
                    Log.v("E_VALUE", "EROR MAN");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */

        //Simple user add;
        /*
        mSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mUserName.getText().toString();
                String password = mUserPassword.getText().toString();
                DatabaseReference myRef = database.getReference(name);
                myRef.setValue(password);
            }
        });
        */
    }
}
