package com.fantasticapps.musiqproto;


import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.R.layout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * TODO: Fix create playlist
 * View: button to add a playlist, text field enter playlist name, and list of playlists
 * Functionalities: create a playist and open a playlist
 */
public class PlaylistActivity extends Fragment {

    final String TAG = "PlaylistActivity";
    final static String EXTRA_KEY = "com.fantasticapps.musiqproto.PlaylistActivity.key";

    private Button create_playlist;
    private EditText title_input;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private DatabaseReference playlistRef;
    private DatabaseReference rootRef;
    private String userID;
    private ArrayList<String> playlist_list;

    ListView list;

    @Override
    public View onCreateView(final LayoutInflater inflaterTwo, final ViewGroup container, Bundle savedInstanceStateTwo) {
        super.onCreate(savedInstanceStateTwo);

        View rootView = inflaterTwo.inflate(R.layout.activity_playlist, container, false);

        //Authorize and get user data from the database

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        mRef = database.getReference().child("Users").child(userID);
        playlistRef = database.getReference().child("Playlists");
        rootRef = database.getReference();

        list = (ListView) rootView.findViewById(R.id.playlist);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };



        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        title_input = (EditText) rootView.findViewById((R.id.playlist_title_input));
        create_playlist = (Button) rootView.findViewById(R.id.create_Playlist_Button);

        create_playlist.setEnabled(false);

        //If the create playlist button is pressed the text enter in the field will be used to create a playlist
        create_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title_input.toString() != null) {
                    //createPlaylist(title_input.getText().toString());
                }

            }
        });



        //If a string is added to the list, then the create button is enabled
        title_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length() == 0)
                {
                    create_playlist.setEnabled(false);
                }
                else {
                    create_playlist.setEnabled(true);
                }
            }

            //If the text is changed and the field is empty, the create playlist button is disabled
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length() == 0)
                {
                    create_playlist.setEnabled(false);
                }
                else {
                    create_playlist.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return rootView;

    }

    private void showData(final DataSnapshot dataSnapshot) {
        ArrayList<String> array = new ArrayList<>();
        playlist_list = new ArrayList<>();

        final DataSnapshot userPlaylists = dataSnapshot.child("Users").child(userID).child("playlists");
        for(DataSnapshot ds : userPlaylists.getChildren()) {

            playlist_list.add(ds.getValue(String.class));
            String playlistID = ds.getValue(String.class);
            array.add(dataSnapshot.child("Playlists").child(playlistID).child("playlist_name").getValue(String.class));

        }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(PlaylistActivity.this.getContext(), android.R.layout.simple_list_item_1, array);

            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    int position = adapterView.getPositionForView(view);
                    String target = "";
                    String targetKey = "";
                    int currentPosition = 0;
                    for (DataSnapshot ds : userPlaylists.getChildren()) {

                        if (currentPosition == position) {
                            target = ds.getValue(String.class);
                            targetKey = ds.getKey();
                        }
                        currentPosition++;
                    }
                    if (!target.equals("")) {

                        Fragment fragment = new DisplaySongsActivity();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_playlist, fragment, TAG);
                        fragmentTransaction.addToBackStack(TAG);
                        fragmentTransaction.commit();
                    }
                }
            });


    }

    //Create playlist under the specific user in the database
    private void createPlaylist(String title)
    {
        //Get the user's information from the database
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        mRef = database.getReference().child("Users").child(userID);


        /**
         * Create and Add the playlist to the database
         * Create playlist with the current date and value of titles as its name
         * Add the newly created palylist under the user's playlist
         */

        playlistRef = database.getReference().child("Playlists");
        DatabaseReference newPlaylistRef = playlistRef.push();
        newPlaylistRef.child("AuthorKey").setValue(userID);
         //newPlaylistRef.child("create_date").setValue();
        String newPlaylist = newPlaylistRef.getKey();
        newPlaylistRef.child("playlist_name").setValue(title);
        newPlaylistRef.child("create_date").setValue(ServerValue.TIMESTAMP);

        playlist_list.add(newPlaylist);
        mRef.child("playlists").setValue(playlist_list);

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}


/*
                    Intent songs = new Intent(PlaylistActivity.this.getActivity(), DisplaySongsActivity.class);
                    songs.putExtra(Intent.EXTRA_TEXT, target);
                    songs.putExtra(EXTRA_KEY,targetKey);
                    startActivity(songs);
                    */