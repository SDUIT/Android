package com.example.zoir.taqvim;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Zoir on 28-Jul-17.
 */

class taqvimDatabaseHelper extends SQLiteOpenHelper {

    private static final String name = "starbuzz";
    private static final int db_version = 1;

    taqvimDatabaseHelper (Context context) {
         super(context, name, null, db_version);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE DRINK ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME , "
                + "DESCRIPTION TEXT, "
                + "IMAGE_RESOURCE_ID INTEGER);");

    }

    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {

    }
}
