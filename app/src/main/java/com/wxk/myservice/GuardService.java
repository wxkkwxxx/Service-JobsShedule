package com.wxk.myservice;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/4/8
 * 双进程守护
 */

public class GuardService extends Service{


    private int messageId = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ProcessConnection.Stub(){};
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(messageId, new Notification());//提高优先级
        //绑定建立链接
        bindService(new Intent(this, MessageService.class), conn, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    private ServiceConnection conn =  new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(GuardService.this, "建立链接", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开链接 ,重启,重新绑定
            startService(new Intent(GuardService.this, MessageService.class));
            bindService(new Intent(GuardService.this, MessageService.class), conn, Context.BIND_IMPORTANT);
        }
    };
}
