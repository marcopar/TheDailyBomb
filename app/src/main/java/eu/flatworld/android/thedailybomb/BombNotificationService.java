package eu.flatworld.android.thedailybomb;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class BombNotificationService extends IntentService {
    public BombNotificationService() {
        super("BombNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        long[] vibrationPattern = new long[]{0L, 200L, 50L, 200L, 50L, 200L, 50L};

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, Main.NOTIFICATION_CHANNEL)
                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                        .setSmallIcon(R.mipmap.time_bomb_notification)
                        .setContentTitle(getString(R.string.bomb_delivered))
                        .setContentText("")
                        .setVibrate(vibrationPattern)
                        .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    Main.NOTIFICATION_CHANNEL,
                    getString(R.string.notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setVibrationPattern(vibrationPattern);
            channel.enableLights(true);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(pendingIntent);

        notificationManager.notify(0, mBuilder.build());
    }
}
