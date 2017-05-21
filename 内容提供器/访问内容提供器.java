package com.example.ethanwalker.a06_providertest;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static final String AUTHORITY = "com.example.ethanwalker.mycontentprovider.provider";
    private String newId;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 添加数据
        Button addData = (Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://" + AUTHORITY + "/book");
                ContentValues values = new ContentValues();
                values.put("name", "book-name3");
                values.put("author", "author3");
                values.put("pages", 333);
                values.put("price", 33.33);
                Uri newUri = getContentResolver().insert(uri, values);
                newId = newUri.getPathSegments().get(1);
            }

        });

        // 更新数据
        Button updateData = (Button)findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://"+AUTHORITY+"/book/"+newId);
                ContentValues values=  new ContentValues();
                values.put("pages",1111);
                values.put("price",1111.11);
                getContentResolver().update(uri,values,null,null);

            }
        });

        // 查询数据
        Button queryData = (Button)findViewById(R.id.query_data);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://"+AUTHORITY+"/book");
                Cursor cursor = getContentResolver().query(uri,null,null,null,null);
                while(cursor.moveToNext()){
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String author = cursor.getString(cursor.getColumnIndex("author"));
                    int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                    Double price  = cursor.getDouble(cursor.getColumnIndex("price"));
                    Log.e(TAG, name+"=>"+author+"=>"+pages+"=>"+price );

                }
                if(cursor!=null){
                    cursor.close();
                }
            }
        });

        // 删除数据
        Button delData = (Button)findViewById(R.id.delete_data);
        delData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://"+AUTHORITY+"/book");
                getContentResolver().delete(uri,"id = ?",new String[]{"1"});

            }
        });
    }
}
