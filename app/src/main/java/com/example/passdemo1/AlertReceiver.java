package com.example.passdemo1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class AlertReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Alarm has been received "+intent.getStringExtra("test"), Toast.LENGTH_LONG).show();

        NotificationHelper notificationHelper=new NotificationHelper(context);
        NotificationCompat.Builder nb=notificationHelper.getChannel1Notification(intent.getStringExtra("test"),intent.getStringExtra("testone"));
        notificationHelper.getManager().notify(3,nb.build());

    }
}
