package com.ata.tempalarm1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);


        NotificationCompat.Builder mBuilder= new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.ic_launcher_background);
        //mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        mBuilder.setContentTitle("Notification");
        mBuilder.setContentText(intent.getStringExtra("alarmText"));
        mBuilder.setSound(alarmUri);
        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(intent.getStringExtra("alarmText")));
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel= new NotificationChannel("default","default channel", NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
            //mBuilder.setNotificationChannel
            mBuilder.setChannelId("default");
        }

        Notification notification = mBuilder.build();
        notification.flags|=Notification.FLAG_INSISTENT;
        //notification.priority|=Notification.PRIORITY_MAX;

        //mNotificationManager.createNotificationChannel
        mNotificationManager.notify(
                1
                //(int)System.currentTimeMillis()
                ,notification);


    }
}
