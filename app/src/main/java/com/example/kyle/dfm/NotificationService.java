package com.example.kyle.dfm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

public class NotificationService extends Service {

    public static final String CHANNEL_1_ID = "channel1";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);

        Log.i("KNF-SERVICE", "service started");

        createNotificationChannel();

        showNotification();

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        Log.i("KNF-SERVICE", "service stopped");
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /*CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel); */

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_1_ID, "Channel 1", NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("This is Channel 1");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
    }

    private void showNotification() {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        Notification notification = new NotificationCompat.Builder(this,NotificationService.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.dontforget)

                .setContentTitle("You might be forgetting something!")

                .setContentText("Hey! Are you forgetting something?! Come check!")

                .setSmallIcon(android.R.drawable.btn_dropdown)

                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                .build();

        notificationManager.notify(1, notification);
    }
}