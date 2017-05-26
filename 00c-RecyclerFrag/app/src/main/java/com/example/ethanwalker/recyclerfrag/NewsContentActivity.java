package com.example.ethanwalker.recyclerfrag;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NewsContentActivity extends AppCompatActivity {

    public static void actionStart(Context context, String title,String content){
        Intent intent = new Intent(context,NewsContentActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("content",content);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        Intent intent = getIntent();
        if(intent!=null){
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            TextView titleView = (TextView)findViewById(R.id.news_content_title);
            TextView contentView = (TextView)findViewById(R.id.news_content_content);
            titleView.setText(title);
            contentView.setText(content);
        }


    }


}
