package com.example.ethanwalker.myapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,MeFragment.MeCallBack,ChangePersonalFrag.ChangeCallBack,WeixinAdapter.WeixinCallback{
    WeixinFragment weixinFragment;
    MeFragment meFragment;
    Button weixin;
    Button me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weixinFragment = new WeixinFragment();
        change(weixinFragment);

        weixin = (Button)findViewById(R.id.weixin);
        me = (Button)findViewById(R.id.me);

        weixin.setOnClickListener(this);
        me.setOnClickListener(this);

    }
    public void change(Fragment fragment){

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.weixin:
                if(weixinFragment==null){
                    weixinFragment = new WeixinFragment();
                }
                change(weixinFragment);
                break;
            case R.id.me:
                if(meFragment ==null){
                    meFragment = new MeFragment();
                }
                change(meFragment);
                break;
            default:
                break;
        }
    }

    @Override
    public void startChange(View v) {
        ChangePersonalFrag frag = new ChangePersonalFrag();
        getFragmentManager().beginTransaction().replace(R.id.fragment,frag).commit();

    }

    @Override
    public void submitMessage(Bundle bundle) {
        if(meFragment==null){
            meFragment = new MeFragment();
        }
        meFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fragment,meFragment).commit();
    }

    @Override
    public void startChat() {
        Intent intent  = new Intent(MainActivity.this,MsgActivity.class);
        MainActivity.this.startActivity(intent);
    }
}
