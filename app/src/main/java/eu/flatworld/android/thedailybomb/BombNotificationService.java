package eu.flatworld.android.thedailybomb;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.UUID;

public class BombNotificationService extends IntentService {
    private static final int NOTIFICATION_ID = 3;

    public BombNotificationService() {
        super("BombNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this).setChannel(Main.NOTIFICATION_CHANNEL)
                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                        .setSmallIcon(R.mipmap.time_bomb_notification)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!").setVibrate(new long[]{0L, 200L, 50L, 200L, 50L, 200L, 50L});

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra(Main.EXTRA_BOMBID, UUID.randomUUID().toString());
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.setAutoCancel(true).build());
    }
}
