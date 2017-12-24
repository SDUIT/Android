package com.example.zoir.company;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Zoir on 18-Oct-17.
 */

public class Company extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
