package com.fantasticapps.musiqproto;

import android.graphics.drawable.Icon;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Defines the Navigation bar
 * View and functionalities to navigation between the screens: dashboard, playlists, parties, and settings
 */
public class MainActivity extends AppCompatActivity {

    public SectionsPagerAdapterTwo mSectionsPagerAdapterTwo;
    public ViewPager mViewPagerTwo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //To remove the title in the app
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.setContentView(R.layout.activity_main_nav);

        getTabLayout();

    }

    public TabLayout getTabLayout() {

        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        mSectionsPagerAdapterTwo = new SectionsPagerAdapterTwo(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPagerTwo = (ViewPager) findViewById(R.id.container_nav);
        mViewPagerTwo.setAdapter(mSectionsPagerAdapterTwo);

        TabLayout tabLayoutNav = (TabLayout) findViewById(R.id.tabs_nav);

        tabLayoutNav.setupWithViewPager(mViewPagerTwo);

        //Set Icons for each tab
        tabLayoutNav.getTabAt(0).setIcon(R.drawable.ic_dashboard);
        tabLayoutNav.getTabAt(1).setIcon(R.drawable.ic_playlist);
        tabLayoutNav.getTabAt(2).setIcon(R.drawable.ic_action_name);
        tabLayoutNav.getTabAt(3).setIcon(R.drawable.ic_settings);


        return tabLayoutNav;

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapterTwo extends FragmentPagerAdapter {

        public SectionsPagerAdapterTwo (FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    DashBoard dashBoard = new DashBoard();
                    return dashBoard;
                case 1:
                    PlaylistActivity playlistActivity = new PlaylistActivity();
                    return playlistActivity;
                case 2:
                    PartiesActivity partiesActivity = new PartiesActivity();
                    return partiesActivity;
                case 3:
                    SettingsActivity settingsActivity = new SettingsActivity();
                    return settingsActivity;

            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

    }
}
