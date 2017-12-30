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
    public static void deployBomb(Context context) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, BombBroadcastReceiver.class);
        intent.putExtra(Main.EXTRA_BOMBID, UUID.randomUUID().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManagerCompat.setExact(alarmManager, AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (3 * 1000), pendingIntent);
    }
}
