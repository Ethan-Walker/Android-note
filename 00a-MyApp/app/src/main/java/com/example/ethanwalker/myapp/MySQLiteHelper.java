package com.example.ethanwalker.myapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by EthanWalker on 2017/6/9.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = "MySQLiteHelper";

/*    String CREATE_BOOK = "create table Conversation( " +
            "order integer autoincrement, " +
            "name text, " +
            "content text, " +
            "primary key(order, name) )";*/
    String CREATE_BOOK = "create table Conversation(id integer increment,name text,content text,primary key(id))";
    Context mContext;

    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG, "onCreate: ");
        db.execSQL(CREATE_BOOK);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
