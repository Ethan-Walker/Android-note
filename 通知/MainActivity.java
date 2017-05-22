package com.example.ethanwalker.a07_phoneresource;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        Button notify1 = (Button) findViewById(R.id.notify1);
        notify1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,NotificationActivity.class);
                PendingIntent pi = PendingIntent.getActivity(mContext,0,intent,0);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = new NotificationCompat.Builder(MainActivity.this)
                        .setContentTitle("this is notification title")
//                        .setContentText("this is notification content")
/*                        .setStyle(new NotificationCompat.BigTextStyle().bigText("LAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                                "AAAAAAAAAAaaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                                "aAAAAAAAAAAAAAAAAAAAAAA"))*/
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.a)))
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.large))
                        .setContentIntent(pi)
                        .setAutoCancel(true)
                        .setSound(Uri.fromFile(new File("/system/media/audio/ringtones/DreamTheme.ogg")))
                        .setVibrate(new long[]{0,1000,1000,1000,1000,1000})
                        .setLights(Color.GREEN,1000,1000)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .build();

                notificationManager.notify(1, notification);

                // 铃声文件可以在DDMS中对应的文件夹下找到
                // setPriority(NotificationCompat.PRIORITY_MIN)
                // MIN : 无提示声音 HIGH/MAX： 弹出横条通知
            }

        });


    }
}
