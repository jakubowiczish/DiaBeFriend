package com.example.diabefriend.model;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class NotificationChannels extends Application {
    public static final String CHANNEL_ONE_ID = "channel_high";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channelOne = new NotificationChannel(
                    CHANNEL_ONE_ID,
                    "channel one",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channelOne.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channelOne.enableLights(true);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channelOne);
        }
    }
}
