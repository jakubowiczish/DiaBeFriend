package com.example.diabefriend.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

import com.example.diabefriend.R;
import com.example.diabefriend.activities.SummaryActivity;
import com.example.diabefriend.activities.TimerActivity;

import androidx.core.app.NotificationCompat;

public class Alarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, SummaryActivity.class);
        intent1.putExtra(TimerActivity.measurementString, intent.getParcelableExtra(TimerActivity.measurementString));
        context.startActivity(intent1);
        Toast.makeText(context, "IT IS TIME TO TEST YOUR SUGAR LEVEL!", Toast.LENGTH_LONG).show();
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        MediaPlayer mp = MediaPlayer.create(context, notification);
        mp.start();
    }
}
