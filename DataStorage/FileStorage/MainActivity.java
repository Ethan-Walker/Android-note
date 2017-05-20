package com.example.ethanwalker.a03_datestorage;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText)findViewById(R.id.username);
        String storedInput = load();

        // TextUtils.isEmpty(str)   当字符串为null或者"" 时，都会返回true
        if(!TextUtils.isEmpty(storedInput)) {
            username.setText(storedInput);
            // 将光标移到文本末位置，方便继续输入
            username.setSelection(storedInput.length());
            Toast.makeText(this,"restoring succeeded...",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String user = username.getText().toString();
        save(user);

    }
    public String load(){
        StringBuilder stringBuilder = new StringBuilder();
        FileInputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = openFileInput("username");
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();

    }
    public void save(String data){
        FileOutputStream fileOutputStream = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileOutputStream = openFileOutput("username",MODE_APPEND);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            bufferedWriter.write(data);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bufferedWriter!=null){
                try {
                    // 只需要关闭最外层的流，内部流就会自动关闭
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
