package com.example.ethanwalker.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.ethanwalker.a10_multithread.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by EthanWalker on 2017/5/26.
 */

public class MyHandler extends Thread {

    private static final String TAG = "MyHandler";
    private Handler handler ;
    private String responseData;

    @Override
    public void run() {
        Looper.prepare();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 0x100:
                        Log.e(TAG, "handleMessage: " );
                        sendReq(msg.getData().getString("url"));
                        break;
                    default:
                        break;
                }
            }
        };
        Looper.loop();
    }

    private void sendReq(String str) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader =null;
        HttpURLConnection connection =null;

        try {
            URL url= new URL(str);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(8000);
            InputStream in = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while((line=reader.readLine())!=null){
                sb.append(line);
            }
            this.responseData = sb.toString();
            Log.e(TAG,responseData);
            sendToMainThread(responseData);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(connection!=null){
                connection.disconnect();
            }
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendToMainThread(String responseData) {
        Log.e("responseData:",responseData+"1" );
        Log.e(TAG, "sendToMainThread: " );
        Message message = new Message();
        message.what = 0x000;
        Bundle bundle = new Bundle();
        bundle.putString("responseData",responseData);
        message.setData(bundle);
        MainActivity2.mainHandler.sendMessage(message);
    }

    public Handler getHandler() {
        return handler;
    }
}
