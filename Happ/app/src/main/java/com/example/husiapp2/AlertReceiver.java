package com.example.husiapp2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Alet","Alarm ringing");
        NotificationHelper helper = new NotificationHelper(context);
        NotificationCompat.Builder nb = helper.getChannelNotification();
        helper.getManager().notify(1,nb.build());
        final MediaPlayer mp = MediaPlayer.create(context,R.raw.plucky);
        mp.start();

    }


}
