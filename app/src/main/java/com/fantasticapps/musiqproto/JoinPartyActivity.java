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
import android.widget.Button;

/**
 * TODO: View and functionalities to join a party
 */
public class JoinPartyActivity extends Fragment {

    private Button memberViewBtn;

    /**
     * TODO: all functionalities and view
     * @param inflaterTwo
     * @param container_nav
     * @param savedInstanceStateTwo
     * @return the join screen
     */
    @Override
    public View onCreateView(final LayoutInflater inflaterTwo, final ViewGroup container_nav, Bundle savedInstanceStateTwo) {
        super.onCreate(savedInstanceStateTwo);

        View rootView = inflaterTwo.inflate(R.layout.activity_create_party, container_nav, false);

        /**
         * Join button clicked -> user taken to member view
         */
        memberViewBtn = (Button) rootView.findViewById(R.id.memberViewBtn);

        memberViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JoinPartyActivity.this.getContext(), MemberView.class));
            }
        });
        return rootView;
    }
}
