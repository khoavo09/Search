package com.fantasticapps.musiqproto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
  
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
  
import static com.fantasticapps.musiqproto.PlaylistActivity.EXTRA_KEY;

/**
 * View: Title of playlist, and buttons to rename and delete the playlist
 * Functionality: Rename, delete a specific playlist,
 * And add, delete, and view songs in the playlist
 */

public class DisplaySongsActivity extends Fragment {

    private Button rename_playlist_button, delete_playlist_button;
    private TextView playlist_title_header;

    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceStateTwo) {
        super.onCreate(savedInstanceStateTwo);

        View rootView = inflater.inflate(R.layout.activity_display_songs, container, false);


        //Get user information from the database
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();

        Intent playlistIntent = this.getActivity().getIntent();
        String playlistKey = playlistIntent.getStringExtra(Intent.EXTRA_TEXT);
        String userPlaylistKey = playlistIntent.getStringExtra(EXTRA_KEY);

        mRef = database.getReference().child("Playlists").child(playlistKey);
        userRef = database.getReference().child("Users").child(userID).child("playlists").child(userPlaylistKey);

        rename_playlist_button = (Button)rootView.findViewById(R.id.rename_playlist_button);
        playlist_title_header = (TextView) rootView.findViewById(R.id.playlist_title_header);
        delete_playlist_button = (Button)rootView.findViewById(R.id.delete_playlist_button);

        //If the rename button is pressed -> a dialog with the option to rename the playlist
        rename_playlist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DisplaySongsActivity.this.getActivity());
                builder.setTitle("Input new playlist title");

                // Set up the input
                final EditText input = new EditText(DisplaySongsActivity.this.getActivity());
               // Specify the type of input expected
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                builder.setView(input);

                //If the OK button is pressed, the playlist is renamed
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        renamePlaylist(input.getText().toString());
                    }
                });
                //If the cancel button is pressed, the playlist name doesn't change
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }

        });

        //If the delete button is pressed -> the user is given the option to delete the playlist
        delete_playlist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DisplaySongsActivity.this.getActivity());
                String title = playlist_title_header.getText().toString();
                builder.setTitle("Delete " + title + "?");

                //If the delete button is pressed -> the playlist is deleted from the database
                //User is taken back to the Playlists screen
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent goback = new Intent(DisplaySongsActivity.this.getActivity(), PlaylistActivity.class);
                        startActivity(goback);
                        mRef.setValue(null);
                        userRef.setValue(null);
                    }
                });

                //If the cancel button is pressend -> no changes are made
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
         });

                //Checks for change with the song data in the database, and accordinly updates the view
                mRef.child("song_data").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        displaySongs(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        //Checks for changes with the playlist and updates the view accordingly
        mRef.child("playlist_name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                displayPlaylistName(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    private void displaySongs(DataSnapshot dataSnapshot) {

    }

    //To display the name of the playlist
    private void displayPlaylistName(DataSnapshot dataSnapshot) {
        String title = dataSnapshot.getValue(String.class);
        playlist_title_header.setText(title);
    }

    //To rename the playlist in the database
    private void renamePlaylist(String s) {
        mRef.child("playlist_name").setValue(s);
    }
}