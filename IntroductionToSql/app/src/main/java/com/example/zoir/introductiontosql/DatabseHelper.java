package com.example.zoir.introductiontosql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Zoir on 8-Sep-17.
 */

public class DatabseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_Name = "Student.db";
    public static final String TABLE_Name = "student_table";
    public static final String Col_1 = "ID";
    public static final String Col_2 = "NAME";
    public static final String Col_3 = "SURNAME";
    public static final String Col_4 = "MARKS";

    public DatabseHelper(Context context) {
        super(context, DATABASE_Name, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
          db.execSQL("create table " + TABLE_Name + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,SURNAME TEXT,MARKS INTEGER)" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
          db.execSQL("DROP TABLE IF EXISTS " + TABLE_Name);
          onCreate(db);
    }
}
