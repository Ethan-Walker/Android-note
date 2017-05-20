package com.example.ethanwalker.SqliteDemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ethanwalker.a03_datestorage.R;

public class Main3Activity extends AppCompatActivity {
    private Context mContext;
    MySQLiteHelper mySQLiteHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        mContext = this;
        mySQLiteHelper = new MySQLiteHelper(mContext, "BookStore.db", null, 2);
        Button button = (Button) findViewById(R.id.sqlite_storage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySQLiteHelper.getWritableDatabase();
            }
        });

        Button addData = (Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
/*
                ContentValues values = new ContentValues();
                values.put("name", "The Da Vinci Code");
                values.put("pages", 454);
                values.put("author", "Dan Brown");
                values.put("price", 16.96);
                db.insert("Book", null, values);

                values.put("name", "The Lost Symbol");
                values.put("pages", 510);
                values.put("author", "Dan Brown");
                values.put("price", 19.95);
                db.insert("Book", null, values);
*/
                db.execSQL("insert into Book values(null,?,?,?,?)", new Object[]{"zhangsan", 219.00, 499, "Computer Network"});
                Toast.makeText(mContext, "add succeeded...", Toast.LENGTH_LONG).show();


            }
        });
        Button updateData = (Button) findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
/*
                ContentValues values = new ContentValues();
                values.put("price", 100.00);
                //  db.update(String table,ContentValues values, String whereClause,String[] whereArgs);
              db.update("Book",values,"pages=?",new String[]{"454"});
*/
                db.execSQL("update Book set price = 100.00 where pages > ? and pages < ?", new Object[]{400, 500});
                Toast.makeText(mContext, "update succeeded...", Toast.LENGTH_SHORT).show();
            }
        });


        Button deleteData = (Button) findViewById(R.id.delete_data);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
//                db.delete("Book","pages > ?",new String[]{"100"});
                db.execSQL("delete from Book where price = ?", new Object[]{100.00});
                Toast.makeText(mContext, "delete succeeded...", Toast.LENGTH_LONG).show();
            }
        });

        Button selectData = (Button) findViewById(R.id.select_data);
        selectData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
//                Cursor cursor = db.query("Book",null,null,null,null,null,null,null);
                Cursor cursor = db.rawQuery("select * from Book", null);
                if (cursor.moveToFirst()) {
                    // 指针从第一位开始，循环向下遍历
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        Toast.makeText(mContext, name + "=>" + price + "=>" + author + "=>" + pages, Toast.LENGTH_LONG).show();

                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        });
    }
}
