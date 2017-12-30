package eu.flatworld.android.thedailybomb;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class BombScreen extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bomb_screen, container, false);
        Toast.makeText(getActivity(), getArguments().getString(Main.EXTRA_BOMBID), Toast.LENGTH_LONG).show();
        return v;
    }
}
