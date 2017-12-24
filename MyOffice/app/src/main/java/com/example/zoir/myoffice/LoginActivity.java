package com.example.zoir.myoffice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField;

    private Button mregBtn;
    private Button mSignBtn;
    private Button mLogoutBtn;

    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mregBtn = (Button) findViewById(R.id.regBtn);
        mSignBtn = (Button) findViewById(R.id.signBtn);
        mLogoutBtn = (Button) findViewById(R.id.logoutBtn);

        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);

        mregBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        mSignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });
    }
    private void checkLogin() {

        String email = mEmailField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mProgress.setMessage("Loading...");
            mProgress.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                    if( task.isSuccessful() ) {
                        checkUserExist();
                    }
                    else {
                        mProgress.cancel();
                        Toast.makeText(getApplicationContext(), "Eror Login" , Toast.LENGTH_LONG).show();
                    }
                 }
             });
        }
        else {
            Toast.makeText(getApplicationContext(), "Make sure to Fill" , Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUserExist() {
        final String user_id = mAuth.getCurrentUser().getUid();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 if( dataSnapshot.hasChild(user_id) ) {

                     mProgress.cancel();
                     Intent mainActivity = new Intent(LoginActivity.this,MainActivity.class);
                     mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                     startActivity(mainActivity);

                     mEmailField.setText("");
                     mPasswordField.setText("");
                 }
                 else {
                     mProgress.cancel();
                     Toast.makeText(LoginActivity.this, "You need to Setup your Account", Toast.LENGTH_LONG).show();
                 }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
