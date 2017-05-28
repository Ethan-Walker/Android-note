package com.example.ethanwalker.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ethanwalker.a10_multithread.R;

/**
 * Created by EthanWalker on 2017/5/26.
 */

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = "MainActivity2";
    Button sendReq;
    TextView textView;
    static Handler mainHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        sendReq = (Button)findViewById(R.id.change_text2);
        textView = (TextView)findViewById(R.id.text_view2);
        final MyHandler myHandler = new MyHandler();
        myHandler.start();
        sendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = 0x100;
                Bundle bundle = new Bundle();
                bundle.putString("url","https://www.baidu.com");
                message.setData(bundle);
                myHandler.getHandler().sendMessage(message);
            }
        });
        mainHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0x000){
                    Log.e(TAG, "handleMessage:" );
                    String data = msg.getData().getString("responseData");
                    textView.setText(data);
                }
            }
        };

    }

}
