package com.example.ethanwalker.asynctask;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ethanwalker.a10_multithread.R;

/**
 * Created by EthanWalker on 2017/5/27.
 */

public class MainActivity3 extends AppCompatActivity{

    private Context mContext;
    private TextView textView;
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mContext = this;
        textView = (TextView)findViewById(R.id.text_view2);
        button = (Button)findViewById(R.id.change_text2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAsyncTask myAsyncTask = new MyAsyncTask(mContext);
                myAsyncTask.execute("http://10.22.56.18:88/get_data.xml");
            }
        });
    }

    public TextView getTextView() {
        return textView;
    }

    public Button getButton() {
        return button;
    }
}
