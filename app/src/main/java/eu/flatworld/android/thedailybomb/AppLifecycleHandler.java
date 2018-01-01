package eu.flatworld.android.thedailybomb;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by marcopar on 01/01/18.
 */

public class AppLifecycleHandler implements Application.ActivityLifecycleCallbacks {
    private int resumed;
    private int started;

    private boolean isVisible = false;
    private boolean isInForeground = false;

    private static AppLifecycleHandler instance;

    public static AppLifecycleHandler getInstance() {
        if (instance == null) {
            instance = new AppLifecycleHandler();
        }

        return instance;
    }

    private AppLifecycleHandler() {
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resumed;
        Log.w(Main.LOGTAG, "onActivityResumed -> application is in foreground: " + (resumed > 0) + " (" + activity.getClass() + ")");
        setForeground((resumed > 0));
    }

    @Override
    public void onActivityPaused(Activity activity) {
        --resumed;
        Log.w(Main.LOGTAG, "onActivityPaused -> application is in foreground: " + (resumed > 0) + " (" + activity.getClass() + ")");
        setForeground((resumed > 0));
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
        Log.w(Main.LOGTAG, "onActivityStarted -> application is visible: " + (started > 0) + " (" + activity.getClass() + ")");
        setVisible((started > 0));
    }

    @Override
    public void onActivityStopped(Activity activity) {
        --started;
        Log.w(Main.LOGTAG, "onActivityStopped -> application is visible: " + (started > 0) + " (" + activity.getClass() + ")");
        setVisible((started > 0));
    }

    private void setVisible(boolean visible) {
        if (isVisible == visible) {
            // no change
            return;
        }

        // visibility changed
        isVisible = visible;
        Log.w(Main.LOGTAG, "App Visiblility Changed -> application is visible: " + isVisible);

        // take some action on change of visibility
    }

    private void setForeground(boolean inForeground) {
        if (isInForeground == inForeground) {
            // no change
            return;
        }

        // in foreground changed
        isInForeground = inForeground;
        Log.w(Main.LOGTAG, "App In Foreground Changed -> application is in foreground: " + isInForeground);

        // take some action on change of in foreground

    }

    public static boolean isApplicationVisible() {
        return AppLifecycleHandler.getInstance().started > 0;
    }

    public static boolean isApplicationInForeground() {
        return AppLifecycleHandler.getInstance().resumed > 0;
    }
}