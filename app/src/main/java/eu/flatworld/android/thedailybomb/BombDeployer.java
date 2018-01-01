package eu.flatworld.android.thedailybomb;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.AlarmManagerCompat;

import java.util.UUID;

/**
 * Created by marcopar on 29/12/17.
 */

public class BombDeployer {
    public static void deployBomb(Context context, long when) {
        Bomb bomb = Main.getCurrentBomb(context);
        if (bomb == null) {
            bomb = new Bomb();
            bomb.setId(UUID.randomUUID().toString());
            bomb.setTimestamp(when);
            Main.setCurrentBomb(context, bomb);
        }
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, BombBroadcastReceiver.class);
        intent.putExtra(Main.EXTRA_BOMBID, bomb.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManagerCompat.setExact(alarmManager, AlarmManager.RTC_WAKEUP, bomb.getTimestamp(), pendingIntent);
    }
}
