package com.example.ethanwalker.bestparctice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.ethanwalker.a11_servicetest.MainActivity;
import com.example.ethanwalker.a11_servicetest.R;

import java.io.File;

// Service 继承Context

public class DownloadService extends Service {

    private static final String TAG = "DownloadService";
    private DownloadTask downloadTask;
    private DownloadBinder mBinder = new DownloadBinder();
    NotificationManager manager;
    private boolean isPaused = false;
    private int hasDownloadProgress = 0;
    private String url = new String();

    private DownloadListener listener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            hasDownloadProgress = progress;
            manager.notify(1, getNotification("正在下载...", progress));
        }

        @Override
        public void onSuccess() {
            Log.e(TAG, "onSuccess: ");
            downloadTask = null;
            manager.cancel(1);  // 取消下载服务
            stopForeground(true);        // 取消前台服务
            Toast.makeText(DownloadService.this, "下载成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            Log.e(TAG, "onFailed: ");
            downloadTask = null;
            stopForeground(true);
            manager.notify(1, getNotification("下载失败", -1));
            Toast.makeText(DownloadService.this, "下载失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused(int progress) {
            Log.e(TAG, "onPaused: ");
            stopForeground(true);
            downloadTask = null;
            hasDownloadProgress = progress;
            manager.notify(1, getNotification("暂停下载", progress));
            Toast.makeText(DownloadService.this, "暂停下载", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            Log.e(TAG, "onCanceled: ");
            downloadTask = null;
            stopForeground(true);
            manager.notify(1, getNotification("取消下载", -1));
            Toast.makeText(DownloadService.this, "取消下载", Toast.LENGTH_SHORT).show();
        }
    };

    // 作为外界活动 调用服务功能的接口
    class DownloadBinder extends Binder {

        public void startDownload(String str) {
            isPaused =false;
            url = str;
            // 一个downloadTask  只能调用一次 execute() 方法, 故暂停下载之后应该丢弃原先的downloadtask
            // 重新创建新的downloadTask对象
            //避免每次点击按钮时，都创建一个新的对象

            if(downloadTask == null){
                downloadTask = new DownloadTask(listener);
                downloadTask.execute(url);
                startForeground(1, getNotification("正在下载...", hasDownloadProgress));
                Toast.makeText(DownloadService.this, "开始下载", Toast.LENGTH_SHORT).show();
            }

        }

        public void pauseDownload() {
            isPaused = true;

            if (downloadTask != null) {
                downloadTask.pausedDowoload();
            }
        }

        public void cancelDownload() {
            if (downloadTask != null) {
                downloadTask.cancelDownload();
            }else{
                // 这样设置是当 暂停之后, downloadTask 被设置为空，再点击cancelDownload 时，不能调用downloadTask.cancelDownload()方法清空文件
                // 设置了isPaused 就能判断已经被暂停，则在这里手动删除文件
                if (isPaused) {
                    String path = Environment.getExternalStorageDirectory().getPath();
                    String fileName = url.substring(url.lastIndexOf("/"));
                    File file = new File(path + fileName);
                    if (file.exists()) {
                        file.delete();
                        Log.e("暂停之后，按取消按钮", "文件已被删除");
                        listener.onCanceled();
                    }
                }
            }
           /* if (isPaused) {
                isPaused = false;
                Log.e(TAG, "cancelDownload: ");
                downloadTask = null;
                stopForeground(true);
                manager.notify(1, getNotification("取消下载", -1));
                Toast.makeText(DownloadService.this, "取消下载", Toast.LENGTH_SHORT).show();
            }*/
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind: ");
        return mBinder;
    }

    private Notification getNotification(String title, int progress) {
        Intent intent = new Intent(this, MainActivity2.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        if (progress >= 0) {
            builder.setContentText("已经下载了 " + progress + "%")
                    .setProgress(100, progress, false);
            // setProgress 会生成ProgressBar显示进度条，
        }
        return builder.build();
    }

    @Override
    public void onCreate() {
        // 在服务被创建时/创建后，才能获取服务的 Notification 对象
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
}
