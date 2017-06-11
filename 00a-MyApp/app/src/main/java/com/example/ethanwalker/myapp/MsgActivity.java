package com.example.ethanwalker.myapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EthanWalker on 2017/4/15.
 */

public class MsgActivity extends AppCompatActivity {

    private static final String TAG = "MsgActivity";
    private List<Msg> msgList = new ArrayList<Msg>();

    private EditText inputText;

    private Button send;

    private RecyclerView msgRecyclerView;

    private MsgAdapter adapter;
    MySQLiteHelper helper = MainActivity.mySQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);

        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        Intent intent = getIntent();
        final String friendName = intent.getStringExtra("friendName");
        Log.e(TAG, "friendName= "+friendName );
        initMsgs(friendName); // 初始化消息数据

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                    inputText.setText(""); // 清空输入框中的内容
                    storeInSQLite(content, friendName);
                }
            }
        });
    }

    public void storeInSQLite(String content, String friendName) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "insert into Conversation values(null,?,?)";
        db.execSQL(sql, new Object[]{friendName, content});
    }

    private void initMsgs(String friendName) {
        Msg msg1 = new Msg("Hello guy.", Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("Hello. Who is that?", Msg.TYPE_SENT);
        msgList.add(msg2);
        Msg msg3 = new Msg("This is Tom. Nice talking to you. ", Msg.TYPE_RECEIVED);
        msgList.add(msg3);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select content from Conversation where name=?", new String[]{friendName});
        if(cursor.moveToFirst()){
            do{
                String content = cursor.getString(cursor.getColumnIndex("content"));
                Msg msg = new Msg(content,Msg.TYPE_SENT);
                msgList.add(msg);
            }while(cursor.moveToNext());
        }
        cursor.close();
    }

}
