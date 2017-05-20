package com.example.ethanwalker.a04_litepalprac;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.DateSorter;
import android.widget.Button;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.ClusterQuery;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        // 初始化数据库（创建数据库，创建表）
        SQLiteDatabase db = LitePal.getDatabase();
        //      等同于      Connector.getDatabase();

        //插入数据
        Button insertValue = (Button) findViewById(R.id.insert_value);
        insertValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Album album = new Album();
                album.setName("album");
                album.setPrice(129.0f);
                album.setCover(new byte[]{1, 2, 3, 4});
                album.save();
                // DataSupport 内的save方法，将数据存储到数据库
                Album album2 = new Album();
                album2.setName("album2");
                album2.setPrice(329.0f);
                album2.setCover(new byte[]{2, 3, 4, 5});
                album2.save();

                Song song1 = new Song();
                song1.setName("song1");
                song1.setDuration(320);
                song1.setAlbum(album);
                song1.save();

                Song song2 = new Song();
                song2.setName("song2");
                song2.setDuration(356);
                song2.setAlbum(album);
                song2.save();

                Toast.makeText(mContext, "insert values succeeded...", Toast.LENGTH_SHORT).show();

            }
        });

        // 更新数据
        Button updateData = (Button) findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*              //1. （同一）保存元组的覆盖，通过DataSupport.find(Album.class,4) 找到id 为 4 的元组
                Album album = DataSupport.find(Album.class,4);
                album.setPrice(100.0f);
                album.save();
*/
/*
                // 2.通过Album继承的 update(id)更新
                Album album = new Album();
                album.setPrice(1000.0F);
                album.update(5);
*/
                // 3. 通过Album继承的UpdateAll()更新模糊条件的元组
                Album album = new Album();
                album.setPrice(1.00F);
                album.updateAll("price >= ?", "100.00");
                Toast.makeText(mContext, "update succeeded...", Toast.LENGTH_SHORT).show();
            }
        });

        // 删除数据
        Button deleteData = (Button) findViewById(R.id.delete_data);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1. 通过DataSuppert.delete(Song.class,id)
                DataSupport.delete(Song.class, 5);

                //2. 通过 DataSupport.deleteAll(Song.class,"duration > ?","350"); 删除模糊条件的元组
                DataSupport.deleteAll(Song.class, "duration > ?", "350");
            }
        });

        //查询数据
        Button queryData = (Button) findViewById(R.id.query_data);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注意查询返回的是单个元组或者元组集合

                //1.
                Song song = DataSupport.find(Song.class, 7);

                //2.
                List<Song> songs1 = DataSupport.findAll(Song.class);

                    //3. 复杂条件查询
                List<Song> songs2 = DataSupport.where("name like ?", "song%").order("duration").find(Song.class);

                Log.e(TAG, song.getName() + "=>" + song.getAlbum() + "=>" + song.getDuration());
                Log.e(TAG, songs1.toString());
                Log.e(TAG, songs2.toString());


                // findFirst()/findLast()

                //4. select(att1,att2) 指明查询哪几列

//                 DataSupport.select("name","duration").find(Song.class);

                /*
                除了 .find(),其他方法都返回ClusterQuery 对象，可以采用连缀写法
                 DataSupport
                 .select(att1,att2)   指明查询哪几列
                 .where("name like ?", "song%")
                 .order("price desc") 按价格降序
                 .limit(a)   只查询前a条元组   limit(3) 查询第1、2、3 条数据
                 .offset(begin) 从第begin+1条元组查询，开始位置是1 不是 0
                    .limit(3).offset(1) 从第2条元组查询，查询 第2、3、4数据
                 .find()  限制查询哪个表，如Book.class  ，一般位置在最后

                */

                /*
                List<Song> songs3 = DataSupport.select("name", "duration")
                        .where("duration > ?", "300")
                        .order("duration")
                        .limit(2)
                        .offset(0)
                        .find(Song.class);
                for (Song temp : songs3) {
                    Toast.makeText(mContext, temp.getName() + "=>" + temp.getDuration() + "=>" + temp.getAlbum(), Toast.LENGTH_SHORT).show();
                }*/

                // 原生sql 语句查询
                Cursor cursor = DataSupport.findBySQL("select * from Song where duration > ? and name like ?","320","song%");
                if(cursor.moveToFirst()){
                    do{
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        int duration = cursor.getInt(cursor.getColumnIndex("duration"));

                        Toast.makeText(mContext, name+ "=>" + duration , Toast.LENGTH_SHORT).show();

                    }while(cursor.moveToNext());
                }
                if(cursor!=null){
                    cursor.close();
                }

            }
        });

    }
}
