package com.example.ethanwalker.httputil;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ethanwalker.a09_webviewtest.R;

/**
 * Created by EthanWalker on 2017/5/24.
 */

public class TestHttpUtil extends AppCompatActivity {

    private static final String TAG = "TestHttpUtil";
    Button sendRequest;
    TextView textView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        sendRequest = (Button) findViewById(R.id.send_request);
        textView = (TextView) findViewById(R.id.display_content);
        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.sendRequest("http://10.22.56.18:88/index.html", new MyInteface() {
                    @Override
                    public void onFinish(String data) {
                        displayOnUi(data);
                    }
                    @Override
                    public void onError(String err) {   
                        Log.e(TAG,err);
                    }
                });
            }
        });
    }
    public void displayOnUi(final String data){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(data);
            }
        });
    }


}
