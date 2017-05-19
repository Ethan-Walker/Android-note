package com.example.ethanwalker.a02_broadcastpractice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by EthanWalker on 2017/5/19.
 */

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    private IntentFilter intentFilter = new IntentFilter();
    private ForceLogoutReceiver receiver = new ForceLogoutReceiver();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        ActivityCollector.add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.remove(this);
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        intentFilter.addAction("com.example.ethanwalker.FORCE_LOGOUT");
        registerReceiver(receiver, intentFilter);
    }

    /*
        @Override
        protected void onPause() {
            super.onPause();
            unregisterReceiver(receiver);
        }*/
    class ForceLogoutReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            Log.e(TAG, "收到下线广播...");

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("警告");
            builder.setMessage("你被强制下线，请重新登录");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCollector.finishAll();
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }
            });
            builder.show();

        }
    }
}
