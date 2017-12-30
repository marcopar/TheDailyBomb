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
        Log.i(Main.LOGTAG, "Bomb received");
        Intent serviceIntent = new Intent(context, BombNotificationService.class);
        serviceIntent.putExtra(Main.EXTRA_BOMBID, intent.getStringExtra(Main.EXTRA_BOMBID));
        context.startService(serviceIntent);
    }
}

