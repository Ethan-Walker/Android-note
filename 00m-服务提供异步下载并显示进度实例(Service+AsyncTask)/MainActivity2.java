package com.example.ethanwalker.bestparctice;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ethanwalker.a11_servicetest.R;

/**
 * Created by EthanWalker on 2017/5/27.
 */

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity2";
    private DownloadService.DownloadBinder binder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected: ");
            binder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder = null;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button startDownload = (Button) findViewById(R.id.start_download);
        Button pauseDownload = (Button) findViewById(R.id.pause_download);
        Button stopDownload = (Button) findViewById(R.id.cancel_download);
        Button bind = (Button) findViewById(R.id.bind);
        startDownload.setOnClickListener(this);
        pauseDownload.setOnClickListener(this);
        stopDownload.setOnClickListener(this);
        bind.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_download:
                binder.startDownload("http://10.22.56.18:88/1.pdf");
                break;
            case R.id.pause_download:
                binder.pauseDownload();
                break;
            case R.id.cancel_download:
                binder.cancelDownload();
                break;
            case R.id.bind:
                Intent intent = new Intent(this, DownloadService.class);
                startService(intent);
                bindService(intent, connection, BIND_AUTO_CREATE);  // 自动创建 Service
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "您拒绝了拒绝读写文件权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 必须保证 Service已经绑定注册，才能调用该方法
        unbindService(connection);
    }
}
