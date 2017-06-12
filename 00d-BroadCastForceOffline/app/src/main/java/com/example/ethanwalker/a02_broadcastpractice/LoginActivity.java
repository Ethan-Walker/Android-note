package com.example.ethanwalker.a02_broadcastpractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {
    EditText username;
    EditText psw;
    Button login;
    CheckBox rememberPass;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        psw = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        rememberPass = (CheckBox) findViewById(R.id.remember_psw);
        preferences = getSharedPreferences("user", MODE_PRIVATE);

        load();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = username.getText().toString();
                String password = psw.getText().toString();
                if (account.equals("admin") && password.equals("123456qq")) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username", username.getText().toString());

                    if (rememberPass.isChecked()) {
                        editor.putString("password", psw.getText().toString());
                        editor.putBoolean("remPass", true);

                    } else {
                        editor.putBoolean("remPass", false);
                    }
                    editor.apply();

                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "用户名或者密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void load(){
        String userValue = preferences.getString("username", "");
        boolean remPass = preferences.getBoolean("remPass", false);
        if (remPass) {
            String passValue = preferences.getString("password", "");
            psw.setText(passValue);
        }
        if (!TextUtils.isEmpty(userValue)) {
            username.setText(userValue);
        }

    }


}
