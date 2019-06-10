package com.example.passdemo1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {

    public static final String channel1id="channel1id";
    public static final String channel2id="channel2id";
    public static final String channel1name="Scheduled Class";
    public static final String channel2name="Available Class";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
            createChannels();
    }

    public void createChannels(){
        NotificationChannel channel1=new NotificationChannel(channel1id,channel1name, NotificationManager.IMPORTANCE_HIGH);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.colorPrimary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(channel1);

        NotificationChannel channel2=new NotificationChannel(channel2id,channel2name, NotificationManager.IMPORTANCE_LOW);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.colorPrimary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(channel2);
    }

    public NotificationManager getManager(){
        if(mManager==null){
            mManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannel1Notification(String title,String message){
        return  new NotificationCompat.Builder(getApplicationContext(),channel1id)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.passlogo);
    }

    public NotificationCompat.Builder getChannel2Notification(String title,String message){
        return  new NotificationCompat.Builder(getApplicationContext(),channel2id)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.passlogo);
    }
}

