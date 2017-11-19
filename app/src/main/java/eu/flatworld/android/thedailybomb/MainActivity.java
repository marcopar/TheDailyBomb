package eu.flatworld.android.thedailybomb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import eu.flatworld.android.thedailybomb.eventbus.SignInEvent;
import eu.flatworld.android.thedailybomb.eventbus.SignOutEvent;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private GoogleApiClientHelper googleApiClientHelper;

    private static int RC_SIGN_IN = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        // Create the Google Api Client with access to the Play Games services
        googleApiClientHelper = new GoogleApiClientHelper(this);


        setContentView(R.layout.main_activity);
        if (getIntent().getStringExtra(Main.EXTRA_BOMBID) != null) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new BombScreen()).commit();
        } else {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new MainScreen()).commit();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        googleApiClientHelper.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        if (googleApiClientHelper.isConnected()) {
            googleApiClientHelper.disconnect();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RC_SIGN_IN) {
            googleApiClientHelper.setSignInClicked(false);
            googleApiClientHelper.setResolvingConnectionFailure(false);
            if (resultCode == RESULT_OK) {
                Log.i(Main.LOGTAG, "Signed in");
                googleApiClientHelper.connect();
            } else {
                Log.i(Main.LOGTAG, "Sign in failed");
                BaseGameUtils.showActivityResultError(this, requestCode, resultCode, R.string.signin_other_error);
            }
        }
    }

    @Subscribe
    public void onSignInEvent(SignInEvent event) {
        googleApiClientHelper.setSignInClicked(true);
        googleApiClientHelper.connect();
    }

    @Subscribe
    public void onSignOutEvent(SignOutEvent event) {
        googleApiClientHelper.setSignInClicked(false);
        if (googleApiClientHelper.isConnected()) {
            Log.i(Main.LOGTAG, "Signed out");
            Games.signOut(googleApiClientHelper.getGoogleApiClient());
            googleApiClientHelper.disconnect();
        } else {
            Log.i(Main.LOGTAG, "Already signed out");
        }
    }

}
