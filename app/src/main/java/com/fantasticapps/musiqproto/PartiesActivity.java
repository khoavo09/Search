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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * View: List of parties and a create button
 * Functionality: user can join a nearby party or from the recently joined party list, or create their own party
 */
public class PartiesActivity extends Fragment {

    final String TAG = "PartiesActivity";

    private Button create_party_button;
            //, join_party_button;
    ListView partyListView, recentlyJoinedListView;
    private DatabaseReference rootRef;
    private FirebaseDatabase database;
    private View rootView;

    /*Dummy values*/
    String[] parties = new String[] { "SJSU",
            "John Smith's Party",
            "Janet's Party"
    };
    String[] recentlyParties = new String[] { "Kim's Party",
            "Justin's Party",
            "James's Party"
    };

    @Override
    public View onCreateView(final LayoutInflater inflaterTwo, final ViewGroup container, Bundle savedInstanceStateTwo) {
        super.onCreate(savedInstanceStateTwo);

        rootView = inflaterTwo.inflate(R.layout.activity_parties, container, false);

        //BottomNavigationView navigation = (BottomNavigationView) rootView.findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        database = FirebaseDatabase.getInstance();
        rootRef = database.getReference();


        //Retrieve list of parties from the database
        partyListView = (ListView) rootView.findViewById(R.id.partyList);
      //  ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.listview, R.id.textView, parties);
      //  partyListView.setAdapter(arrayAdapter);

        //Retrieve parties that the user recently joined
        recentlyJoinedListView = (ListView)rootView.findViewById(R.id.recentlyList);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(PartiesActivity.this.getContext(), R.layout.listview, R.id.textView, recentlyParties);
        recentlyJoinedListView.setAdapter(arrayAdapter2);

        create_party_button = (Button)rootView.findViewById(R.id.create_party_button);
        //join_party_button = (Button) rootView.findViewById(R.id.join_party_button);


        //If create party button is pressed -> user taken to create party view
        create_party_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(PartiesActivity.this.getContext(),CreatePartyActivity.class));
                //v = View.inflate(PartiesActivity.this.getContext(), R.layout.activity_create_party, container_nav);

                Fragment fragment = new CreatePartyActivity();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_parties, fragment, TAG);
                fragmentTransaction.addToBackStack(TAG);
                fragmentTransaction.commit();
            }
        });


        /**
        join_party_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(PartiesActivity.this.getContext(), JoinPartyActivity.class));
                rootView = inflaterTwo.inflate(R.layout.activity_create_party, container_nav, false);
            }
        });
        */

        //If party is created, the party is added to the parties list
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return rootView;
        /*FIX THIS LATER*/
    }

    /**
     * Displays top three parties
     * @param dataSnapshot
     */
    private void showData(final DataSnapshot dataSnapshot) {
       // ArrayList<String> array = new ArrayList<>();
        String[] array = new String[3];

        int i=0;
        DataSnapshot partySnapshot = dataSnapshot.child("Parties");

        for (DataSnapshot tempParty : partySnapshot.getChildren()) {
            Party p = tempParty.getValue(Party.class);
            if(i<3) {
                array[i] = p.getParty_Name();
                i++;
            }
           // array.add(p.getParty_Name());
        }


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PartiesActivity.this.getContext(), R.layout.listview, R.id.textView, array);
        partyListView.setAdapter(arrayAdapter);
       // ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,array);
      /*  partyListView.setAdapter(arrayAdapter);
        partyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int position = adapterView.getPositionForView(view);
                String target = "";
                int currentPosition = 0;
                for(DataSnapshot ds : partyList.getChildren()) {

                    if(currentPosition == position)
                    {
                        target = ds.getValue(String.class);
                    }
                    currentPosition++;
                }
                if(!target.equals("")) {
                    Intent songs = new Intent(PartiesActivity.this, MemberView.class);
                    songs.putExtra(Intent.EXTRA_TEXT, target);
                    startActivity(songs);
                }
            }
        });*/

    }
}
