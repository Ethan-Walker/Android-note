package com.example.ethanwalker.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EthanWalker on 2017/5/27.
 */

public class MyAsyncTask extends AsyncTask<String, Integer, List<Bundle>> {

    private static final String TAG = "MyAsyncTask";
    private Context mContext;
    ProgressDialog progressDialog;

    public MyAsyncTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected List<Bundle> doInBackground(String[] params) {
        List<Bundle> list = new ArrayList<>();

/*        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(params[0]).get().build();
        Response response = null;

        try {
            response = client.newCall(request).execute();
            String responseData = response.body().string();
            list = parseXMLWithPull(responseData);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;*/
        int hasRead = 0;
        HttpURLConnection connection = null;
        StringBuilder sb = new StringBuilder();

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line ="";
            while((line=reader.readLine())!=null){
                sb.append(line);
                hasRead++;
                publishProgress(hasRead);
            }
//            Log.e(TAG,sb.toString());
            list = parseXMLWithPull(sb.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<Bundle> parseXMLWithPull(String responseData) {
        List<Bundle> list = new ArrayList<>();
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            parser = factory.newPullParser();
            parser.setInput(new StringReader(responseData));
            int eventType = parser.getEventType();
            String id = null;
            String name = null ;
            String version =null;
            while (eventType!=XmlPullParser.END_DOCUMENT){
                String tagName = parser.getName();
                if(eventType==XmlPullParser.START_TAG){
                    switch (tagName){
                        case "id":
                            id=parser.nextText();
                            break;
                        case "name":
                            name = parser.nextText();
                            break;
                        case "version":
                            version = parser.nextText();
                            break;
                        default:
                            break;
                    }
                }else if(eventType==XmlPullParser.END_TAG){
                    if(tagName.equals("app")){
                        Bundle bundle = new Bundle();
                        if(tagName.equals("app")){
                            bundle.putString("id",id);
                            bundle.putString("name",name);
                            bundle.putString("version",version);
                        }
                        list.add(bundle);
                    }

                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("读取网页");
        progressDialog.setMessage("正在读取中....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(18);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(List<Bundle> list) {
        StringBuilder sb = new StringBuilder();
        for(Bundle bundle:list){
            sb.append("id:"+bundle.getString("id")+",");
            sb.append("name:"+bundle.getString("name")+",");
            sb.append("version:"+bundle.getString("version"));
            sb.append("\r\n");
        }
        ((MainActivity3)mContext).getTextView().setText(sb.toString());
        progressDialog.dismiss();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Toast.makeText(mContext,"取消下载网页",Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        progressDialog.setProgress(values[0]);
    }
}
