package com.example.ethanwalker.httputil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by EthanWalker on 2017/5/23.
 */

public class HttpUtil {

    public static void sendRequest(final String address, final MyInteface myInterface) {

        new Thread(){
            @Override
            public void run() {
                HttpURLConnection connection = null;
                StringBuilder sb = null;
                BufferedReader br = null;

                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    InputStream in = connection.getInputStream();
                    br = new BufferedReader(new InputStreamReader(in));
                    sb = new StringBuilder();
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                }  catch (IOException e) {
                    e.printStackTrace();
                    myInterface.onError("读取网页内容错误");
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if(br!=null){
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(sb!=null){
                        myInterface.onFinish(sb.toString());
                    }
                }
            }
        }.start();
    }


}
