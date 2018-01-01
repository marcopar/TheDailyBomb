package eu.flatworld.android.thedailybomb;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Main extends Application {
    public final static String LOGTAG = "THEDAILYBOMB";
    public final static String EXTRA_BOMBID = "EXTRA_BOMBID";
    public final static String NOTIFICATION_CHANNEL = "THEDAILYBOMB";

    public final static String PREF_CURRENTBOMB_JSON = "CURRENT_BOMB_JSON";

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(null)
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public static Bomb getCurrentBomb(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String json = pref.getString(Main.PREF_CURRENTBOMB_JSON, null);
        if (json == null) {
            return null;
        }
        return new Bomb(json);
    }

    public static void setCurrentBomb(Context context, Bomb bomb) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        if (bomb != null) {
            String json = bomb.toJson().toString();
            pref.edit().putString(Main.PREF_CURRENTBOMB_JSON, json).commit();
        } else {
            pref.edit().remove(Main.PREF_CURRENTBOMB_JSON).commit();
        }
    }
}
