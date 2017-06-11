package com.example.ethanwalker.myapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.ethanwalker.service.DownloadActivity;
import com.example.ethanwalker.xmlparse.XmlParseTest;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MeFragment.MeCallBack, ChangePersonalFrag.ChangeCallBack, WeixinAdapter.WeixinCallback,FuncFrag.StartCallBack {
    WeixinFragment weixinFragment;
    MeFragment meFragment;
    FuncFrag funcFrag;
    Button weixin;
    Button me;
    Button func;
    static MySQLiteHelper mySQLiteHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weixinFragment = new WeixinFragment();
        change(weixinFragment);

        weixin = (Button) findViewById(R.id.weixin);
        me = (Button) findViewById(R.id.me);
        func = (Button)findViewById(R.id.function);

        weixin.setOnClickListener(this);
        me.setOnClickListener(this);
        func.setOnClickListener(this);

        mySQLiteHelper = new MySQLiteHelper(this,"friends.db",null,1);
        db = mySQLiteHelper.getWritableDatabase();
    }

    public void change(Fragment fragment) {

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.commit();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weixin:
                if (weixinFragment == null) {
                    weixinFragment = new WeixinFragment();
                }
                change(weixinFragment);
                break;
            case R.id.me:
                if (meFragment == null) {
                    meFragment = new MeFragment();
                }
                change(meFragment);
                break;
            case R.id.function:
                if(funcFrag == null){
                    funcFrag = new FuncFrag();
                }
                change(funcFrag);
                break;
            default:
                break;
        }
    }

    @Override
    public void startChange(View v) {
        ChangePersonalFrag frag = new ChangePersonalFrag();
        getFragmentManager().beginTransaction().replace(R.id.fragment, frag).commit();
    }

    @Override
    public void submitMessage(Bundle bundle) {
        if (meFragment == null) {
            meFragment = new MeFragment();
        }
        meFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fragment, meFragment).commit();
    }

    @Override
    public void startChat(String friendName) {
        Intent intent = new Intent(MainActivity.this, MsgActivity.class);
        intent.putExtra("friendName", friendName);
        MainActivity.this.startActivity(intent);
    }
    public void startGetContacts(){
        Intent intent  = new Intent(MainActivity.this,GetContactsActivity.class);
        startActivity(intent);
    }

    @Override
    public void startDownload() {
        Intent intent = new Intent(MainActivity.this, DownloadActivity.class);
        startActivity(intent);
    }

    @Override
    public void startXmlParse() {
        Intent intent = new Intent(MainActivity.this, XmlParseTest.class);
        startActivity(intent);
    }

    private static final String TAG = "MainActivity";
}
