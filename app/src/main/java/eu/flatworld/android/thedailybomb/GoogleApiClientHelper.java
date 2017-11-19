package eu.flatworld.android.thedailybomb;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;

import de.greenrobot.event.Subscribe;
import eu.flatworld.android.thedailybomb.eventbus.SignInEvent;
import eu.flatworld.android.thedailybomb.eventbus.SignOutEvent;

/**
 * Created by marcopar on 03/11/17.
 */

public class GoogleApiClientHelper implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;

    private static int RC_SIGN_IN = 9001;

    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;

    private Activity activity;


    public GoogleApiClientHelper(Activity activity) {

        this.activity = activity;

        // Create the Google Api Client with access to the Play Games services
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void connect() {
        mGoogleApiClient.connect();
    }

    public void disconnect() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public boolean isConnected() {
        return mGoogleApiClient.isConnected();
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
            if (!BaseGameUtils.resolveConnectionFailure(activity, mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, activity.getString(R.string.signin_other_error))) {
                mResolvingConnectionFailure = false;
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
        if (mGoogleApiClient.isConnected()) {
            Log.i(Main.LOGTAG, "Signed out");
            Games.signOut(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        } else {
            Log.i(Main.LOGTAG, "Already signed out");
        }
    }

    public void setSignInClicked(boolean value) {
        mSignInClicked = value;
    }

    public void setResolvingConnectionFailure(boolean value) {
        mResolvingConnectionFailure = value;
    }
}
