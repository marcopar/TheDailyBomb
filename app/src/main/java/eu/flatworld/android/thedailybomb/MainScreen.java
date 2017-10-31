package eu.flatworld.android.thedailybomb;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainScreen extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.main_screen, container, false);

        Button b = (Button) v.findViewById(R.id.bSettings);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsClicked();
            }
        });
        return v;
    }

    private void settingsClicked() {
        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, new SettingsScreen()).commit();
    }

}
