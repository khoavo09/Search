package com.fantasticapps.musiqproto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Class helps transition between login and registeration with the app bar
 */
public class LoginRegisteration extends MainActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login_reg);

        //Set up the tabs to switch between login and register
        getLogRegTabLayout();
    }

    /**
     * View and functionality of the Tabs
     * @return the Tablayout with the two tabs
     */
    public TabLayout getLogRegTabLayout() {

        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container_log_reg);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(mViewPager);

        return tabLayout;

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * @param position: tab that's choosen by user
         * @return Fragment belonging to the position
         * If the first positon is choosen, Login screen is returned
         * If the second postion is choosen, Register screen is returned
         */
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    LoginActivity login = new LoginActivity();
                    return login;
                case 1:
                    RegisterActivity register = new RegisterActivity();
                    return register;
            }
            return null;
        }

        /**
         * @return Number of pages
         */
        @Override
        public int getCount() {
            return 2;
        }

        /**
         * @param position
         * @return The text written in the tab
         * For the first tab, 'LOGIN' is written
         * For the second tab, 'REGISTER' is written
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "LOGIN";
                case 1:
                    return "REGISTER";
            }
            return null;
        }
    }
}