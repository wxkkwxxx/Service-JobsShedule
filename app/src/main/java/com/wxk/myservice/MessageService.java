package com.wxk.myservice;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/4/8
 */

public class MessageService extends Service {

    private static final String TAG = "MessageService";
    private int messageId = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Log.e(TAG, "等待消息");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
//        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(MessageService.this);
//        mNotifyBuilder.setSmallIcon(R.mipmap.ic_launcher)
//                .setTicker("")
//                .setWhen(System.currentTimeMillis())
//                .setContentTitle(getString(R.string.app_name))
//                .setContentText(" ")
//                .setContentIntent(pendingIntent);
//        Notification notification = mNotifyBuilder.build();
        startForeground(messageId, new Notification());//提高优先级
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            //绑定建立链接
            bindService(new Intent(this, GuardService.class), conn, Context.BIND_IMPORTANT);
        }
        return START_STICKY;
    }

    private ServiceConnection conn =  new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //链接上
            Toast.makeText(MessageService.this, "建立链接", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            startService(new Intent(MessageService.this, GuardService.class));
            bindService(new Intent(MessageService.this, GuardService.class), conn, Context.BIND_IMPORTANT);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ProcessConnection.Stub(){};
    }
}
