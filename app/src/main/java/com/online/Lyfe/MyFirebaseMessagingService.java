package com.online.Lyfe;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.online.Lyfe.Online.Activities.ImageScale;
import com.online.Lyfe.Online.Activities.Profile;
import com.online.Lyfe.Online.Container;
import com.online.Lyfe.R;

import java.util.Map;
import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
        String body = remoteMessage.getNotification().getBody();

        Map<String, String> extraData = remoteMessage.getData();

        String url = extraData.get("url");
        String key = extraData.get("key");
        String name = extraData.get("name");
        String text = extraData.get("text");
        String pic = extraData.get("pic");
        String date = extraData.get("date");
        String category = extraData.get("category");
        String USER_ID = extraData.get("user_id");
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "TAC")
                        .setContentTitle(title)
                        .setContentText(body)
                        .setSmallIcon(R.drawable.notifications);

        Intent scale = new Intent();
        assert category != null;
        if (category.equals("post")) {
            scale = new Intent(this, ImageScale.class);
            scale.putExtra("url", url);
            scale.putExtra("key", key);
            scale.putExtra("name", name);
            scale.putExtra("pic", pic);
            scale.putExtra("text", text);
            scale.putExtra("date", date);
            scale.putExtra("category", category);

        } else if (category.equals("follow")) {
            if (USER_ID != null) {
                scale = new Intent(this, Profile.class);
                scale.putExtra("user_id", USER_ID);
            }
        }


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 10, scale,
                PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        int id = (int) System.currentTimeMillis();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("TAC", "demo", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(Color.BLUE);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
        assert notificationManager != null;
        notificationManager.notify(id, notificationBuilder.build());

    }
}