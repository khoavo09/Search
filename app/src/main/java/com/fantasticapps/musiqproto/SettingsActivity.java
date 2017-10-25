package com.fantasticapps.musiqproto;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * TODO: View and functionalities for settings
 */

public class SettingsActivity extends Fragment {

    private final String TAG = "Settings";

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.activity_settings, container, false);

        return rootView;
    }

}