package com.fantasticapps.musiqproto;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * TODO: View and functionalities for user joining a party
 */


public class MemberView extends Fragment {


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceStateTwo) {
        super.onCreate(savedInstanceStateTwo);

        View rootView = inflater.inflate(R.layout.activity_member_view, container, false);

        return rootView;
    }
}
