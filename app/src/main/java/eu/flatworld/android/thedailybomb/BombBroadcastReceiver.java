package eu.flatworld.android.thedailybomb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BombBroadcastReceiver extends BroadcastReceiver {
    public BombBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (AppLifecycleHandler.isApplicationInForeground()) {
            Log.i(Main.LOGTAG, "Bomb received, directly show the bomb screen");
            Intent mainActivityIntent = new Intent(context, MainActivity.class);
            context.startActivity(mainActivityIntent);
        } else {
            Log.i(Main.LOGTAG, "Bomb received, send the notification");
            Intent serviceIntent = new Intent(context, BombNotificationService.class);
            context.startService(serviceIntent);
        }
    }
}

