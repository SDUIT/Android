package com.example.zoir.myoffice;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button mConnect_btn;
    private Button mCreate_btn;
    private Button mLogoutBtn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                   if( firebaseAuth.getCurrentUser()==null ) {
                       Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
                       loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                       startActivity(loginIntent);
                   }
            }
        };


        mConnect_btn = (Button) findViewById(R.id.connect_btn);
        mCreate_btn = (Button) findViewById(R.id.add_btn);
        mConnect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect(mConnect_btn);
            }
        });

        mCreate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create(mCreate_btn);
            }
        });

        mLogoutBtn = (Button) findViewById(R.id.logoutBtn);

        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });

    }
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void connect(View view) {
        intent = new Intent(this, ConnectActivity.class);
        startActivity(intent);
    }
    public void create(View view) {
        intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }
}
