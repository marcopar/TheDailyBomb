package eu.flatworld.android.thedailybomb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by marcopar on 28/10/17.
 */

public class BombBroadcastReceiver extends BroadcastReceiver {
    public BombBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(Main.LOGTAG, "Bomb received");
        Intent intent1 = new Intent(context, BombNotificationService.class);
        context.startService(intent1);
    }
}

