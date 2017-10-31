package eu.flatworld.android.thedailybomb;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Main extends Application {
    public final static String LOGTAG = "THEDAILYBOMB";

    public final static String NOTIFICATION_CHANNEL = "THEDAILYBOMB";

    public final static String EXTRA_BOMBID = "EXTRA_BOMBID";

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(null)
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
