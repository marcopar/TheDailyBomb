package eu.flatworld.android.thedailybomb;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import de.greenrobot.event.EventBus;
import eu.flatworld.android.thedailybomb.eventbus.SignInEvent;
import eu.flatworld.android.thedailybomb.eventbus.SignOutEvent;

public class SettingsScreen extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.settings_screen, container, false);

        Button b = (Button) v.findViewById(R.id.bSignIn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInClicked();
            }
        });
        b = (Button) v.findViewById(R.id.bSignOut);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOutClicked();
            }
        });
        b = (Button) v.findViewById(R.id.bBack);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backClicked();
            }
        });
        b = (Button) v.findViewById(R.id.bTestNotification);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testNotificationClicked();
            }
        });
        return v;
    }

    private void testNotificationClicked() {
        BombDeployer.deployBomb(getActivity(), System.currentTimeMillis() + 3000);
    }


    private void backClicked() {
        getFragmentManager().beginTransaction().replace(R.id.content_frame, new MainScreen()).commit();
    }

    // Call when the sign-in button is clicked
    private void signInClicked() {
        EventBus.getDefault().post(new SignInEvent());
    }

    // Call when the sign-out button is clicked
    private void signOutClicked() {
        EventBus.getDefault().post(new SignOutEvent());
    }

}
