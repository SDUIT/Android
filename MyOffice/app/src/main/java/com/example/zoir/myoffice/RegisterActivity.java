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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private Button mRegisterBtn;
    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPasswordField;

    private ProgressDialog mProgress;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mProgress = new ProgressDialog(this);

        mRegisterBtn = (Button) findViewById(R.id.registerBtn);

        mNameField = (EditText) findViewById(R.id.nameField);
        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegister();
            }
        });
    }

    private void startRegister() {

        final String name = mNameField.getText().toString().trim();
         String email = mEmailField.getText().toString().trim();
         String password = mPasswordField.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) ) {
               mProgress.setMessage("Signing up....");
               mProgress.show();
               mAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                        if( task.isSuccessful() ) {

                            String user_id = mAuth.getCurrentUser().getUid();

                            DatabaseReference current_user_db = mDatabase.child(user_id);

                            current_user_db.child("name").setValue(name);

                            current_user_db.child("Office").child("Connect").setValue("null");
                            current_user_db.child("Office").child("Create").setValue("null");

                            mProgress.cancel();
                            mNameField.setText("");
                            mPasswordField.setText("");
                            mEmailField.setText("");

                            Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent( RegisterActivity.this, LoginActivity.class ));
                        }
                        else {

                            mProgress.cancel();
                            Toast.makeText(getApplicationContext(), "You cant create more than one account with same Email" , Toast.LENGTH_LONG).show();
                        }
                   }
               });
        }
        else {
            Toast.makeText(getApplicationContext(), " You shoud fill all part  " , Toast.LENGTH_SHORT).show();
        }

    }
}
