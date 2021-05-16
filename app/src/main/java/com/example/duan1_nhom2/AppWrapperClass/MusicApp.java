package com.example.duan1_nhom2.AppWrapperClass;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MusicApp extends Application {
    public static final String CONTROLLER_CHANNEL = "Music_Controller";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }
    private void createNotificationChannel(){
        NotificationChannel channel = new NotificationChannel(
                CONTROLLER_CHANNEL,
                "Bảng Điều Khiển",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.setDescription("Bảng Điều Khiển");
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }
}
