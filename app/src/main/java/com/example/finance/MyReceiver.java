package com.example.finance;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    /**
     * called when the BroadcastReceiver is receiving an Intent broadcast.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("旺财");
        builder.setContentText("到记账时间啦！！！");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Notification notification = builder.getNotification();
        manager.notify(1, notification);

        //再次开启LongRunningService这个服务，从而可以
        Intent i = new Intent(context,MainActivity.class);
        context.startService(i);


    }


}

