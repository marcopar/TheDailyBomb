package eu.flatworld.android.thedailybomb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import eu.flatworld.android.thedailybomb.eventbus.SignInEvent;
import eu.flatworld.android.thedailybomb.eventbus.SignOutEvent;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;

    private static int RC_SIGN_IN = 9001;

    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        // Create the Google Api Client with access to the Play Games services
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();


        setContentView(R.layout.main_activity);

        getFragmentManager().beginTransaction().replace(R.id.content_frame, new MainScreen()).commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(Main.LOGTAG, "Connected as " + Games.Players.getCurrentPlayer(mGoogleApiClient).getDisplayName());
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Attempt to reconnect
        Log.i(Main.LOGTAG, "Connection suspended");
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(Main.LOGTAG, "Connection failed with code " + connectionResult.getErrorCode());
        if (mResolvingConnectionFailure) {
            // already resolving
            Log.i(Main.LOGTAG, "Already resolving connection failure");
            return;
        }

        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;
            Log.i(Main.LOGTAG, "Resolving connection failure");
            if (!BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, getString(R.string.signin_other_error))) {
                mResolvingConnectionFailure = false;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                Log.i(Main.LOGTAG, "Signed in");
                mGoogleApiClient.connect();
            } else {
                Log.i(Main.LOGTAG, "Sign in failed");
                BaseGameUtils.showActivityResultError(this, requestCode, resultCode, R.string.signin_other_error);
            }
        }
    }

    @Subscribe
    public void onSignInEvent(SignInEvent event) {
        mSignInClicked = true;
        mGoogleApiClient.connect();
    }

    @Subscribe
    public void onSignOutEvent(SignOutEvent event) {
        mSignInClicked = false;
        if(mGoogleApiClient.isConnected()) {
            Log.i(Main.LOGTAG, "Signed out");
            Games.signOut(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        } else {
            Log.i(Main.LOGTAG, "Already signed out");
        }
    }

}
