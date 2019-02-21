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
import com.example.diabefriend.activities.TimerActivity;

import androidx.core.app.NotificationCompat;

public class Alarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        notifyUserAboutTestingSugarLevel(context);
        Toast.makeText(context, "IT IS TIME TO TEST YOUR SUGAR LEVEL!", Toast.LENGTH_LONG).show();
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        MediaPlayer mp = MediaPlayer.create(context, notification);
        mp.start();
    }

    public void notifyUserAboutTestingSugarLevel(Context context) {
        Intent intent = new Intent(context, TimerActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, intent, 0);

        NotificationCompat.Action action = new NotificationCompat.Action.Builder(
                android.R.drawable.sym_action_chat,
                "Open",
                pendingIntent
        ).build();

        Notification notification = new NotificationCompat.Builder(context, NotificationChannels.CHANNEL_ONE_ID)
                .setSmallIcon(R.drawable.ic_info)
                .setContentTitle("Hey! It's time")
                .setContentText("Test your sugar level!")
                .addAction(action)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }


}
