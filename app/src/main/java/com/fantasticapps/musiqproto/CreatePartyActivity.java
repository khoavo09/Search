package com.fantasticapps.musiqproto;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * View: text field for party name, and radi0 obuttons to get song request setting
 * Functionality: create a party with the inputs entered in the fields and add it to the database
 */
public class CreatePartyActivity extends Fragment {

    private Button createPartyButton;
    private RadioGroup partyOptions;
    private RadioButton selectedOption;
    final String TAG = "Create Party";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private DatabaseReference partyRef;
    private DatabaseReference rootRef;
    private String userID;
    private ListView addSongOptionListView;
    private EditText partyName;
    private View rootView;


    //Options for song request settings for adminster of a party
    String[] options = new String[] { "Admin Only",
            "Member Suggestion Only",
            "Members & Admin"
    };


    @Override
    public View onCreateView(final LayoutInflater inflaterTwo, final ViewGroup container_nav, Bundle savedInstanceStateTwo) {
        super.onCreate(savedInstanceStateTwo);

       rootView = inflaterTwo.inflate(R.layout.activity_create_party, container_nav, false);

        //OLD NAVIGATION CODE
        //BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
       // navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        partyOptions = (RadioGroup) rootView.findViewById(R.id.partyOptions);
        createPartyButton = (Button)rootView.findViewById(R.id.createPartyButton);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        mRef = database.getReference().child("Users").child(userID);
        partyRef = database.getReference().child("Playlists");
        rootRef = database.getReference();

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

        //Party name enter in fild partyNameEdit
        partyName = (EditText) rootView.findViewById(R.id.partyNameEdit);


        /**
         * If the create button is pressed:
         * Song request option is recorded, and party is created and stored in the database
         * User is taken to the adminster view screen
         */
        createPartyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedrule = -1;
                int selectedID = partyOptions.getCheckedRadioButtonId();
                selectedOption = (RadioButton) rootView.findViewById(selectedID);
                //Record user option
                switch (selectedID){
                    case R.id.adminOnlyRadioBtn:
                        selectedrule = 0;
                        break;
                    case R.id.adminAndMembersRadioBtn:
                        selectedrule = 1;
                        break;
                    case R.id.suggestionOnlyRadioBtn:
                        selectedrule = 2;
                        break;
                }

                //Create and store party in database
                createParties(partyName.getText().toString(),selectedrule);

                //Switch to Adminster view
                //startActivity(new Intent(CreatePartyActivity.this.getContext(), AdminView.class));
                Fragment fragment = new AdminView();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_create_party, fragment, TAG);
                fragmentTransaction.addToBackStack(TAG);
                fragmentTransaction.commit();
            }
        });
        return rootView;

    }

    /**
     * @param title: the name of the party
     * @param partyRule: song request option selected
     * A party is created with the choosen parameters
     */
    private void createParties(String title, int partyRule)
    {
        //Get user id from the database and store the new party under parties list by the current user id
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        mRef = database.getReference().child("Users").child(userID);

        partyRef = database.getReference().child("Parties");//get parties reference id under the current user id

        DatabaseReference newPartyRef = partyRef.push();

        //Need more work on these 2
        newPartyRef.child("Active").setValue(true);
        newPartyRef.child("Current Song").setValue(-1);


        //Owner in database if set to current user
        newPartyRef.child("Owner").setValue(userID);

        //Store request option into the database
        String newParty = newPartyRef.getKey();
        partyRef.child(newParty).child("Party_Name").setValue(title);
        switch (partyRule){
            case 0:
                partyRef.child(newParty).child("Party_Rule").setValue(options[0]);
                break;
            case 1:
                partyRef.child(newParty).child("Party_Rule").setValue(options[1]);
                break;
            case 2:
                partyRef.child(newParty).child("Party_Rule").setValue(options[2]);
                break;
        }
       // mRef.child("playlists").push().setValue(newParty);
    }

    //initiate authentication listener
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    //Remove authentication listener
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}


//OLD NAVIGATION CODE
/**

 private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
 = new BottomNavigationView.OnNavigationItemSelectedListener() {

@Override
public boolean onNavigationItemSelected(@NonNull MenuItem item) {
switch (item.getItemId()) {
case R.id.navigation_dashboard:
Intent intent0 = new Intent(CreatePartyActivity.this,DashBoard.class);
startActivity(intent0);
return true;
case R.id.navigation_party:
Intent intent1 = new Intent(CreatePartyActivity.this,PartiesActivity.class);
startActivity(intent1);
return true;
case R.id.navigation_playlist:
Intent intent2 = new Intent(CreatePartyActivity.this,PlaylistActivity.class);
startActivity(intent2);
return true;
case R.id.navigation_settings:
Intent intent3 = new Intent(CreatePartyActivity.this,SettingsActivity.class);
startActivity(intent3);
return true;
}
return false;
}

};
 */