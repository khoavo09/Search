package com.fantasticapps.musiqproto;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executor;

/**
 * View: User name and password text field, button to login w/ the fiels, button to sign in with Google
 * Functinality: Login user with Google or with username and password
 */
public class LoginActivity extends Fragment {

    //Firebase authentication
    private FirebaseAuth mAuth;

    //data for login fields, buttons
    /**
    private Button btnLogin;
    private EditText inputEmail, inputPassword;
    private TabLayout.Tab loginTab;
     */

    final int RC_SIGN_IN = 1;
    final String TAG = "LoginActivity";

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        View rootView = inflater.inflate(R.layout.activity_login, container, false);

        //initialize firebase auth to track sign in/out
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            //TODO: start next activity
        }


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("432757349911-vj8v9l8d8tksf4o78oesfprsve8rk0u4.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        final GoogleApiClient mGoogleApiClient  = new GoogleApiClient.Builder(LoginActivity.this.getContext())
                .enableAutoManage(LoginActivity.this.getActivity() /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        return;
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        rootView.findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                        startActivityForResult(signInIntent, RC_SIGN_IN);
                        break;
                    // ...
                }
            }
        });


        //initilize views
        final EditText inputEmail = (EditText) rootView.findViewById(R.id.loginEmailText);
        final EditText inputPassword = (EditText) rootView.findViewById(R.id.loginPasswordText);
        Button btnLogin = (Button) rootView.findViewById(R.id.buttonConfirmLogin);

        //set login button action
        //If the text fields entered are valid, user is signed in, else user need to enter the information again
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {


                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(v.getContext(), "Please enter your email", Toast.LENGTH_SHORT);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(v.getContext(), "Please enter your password", Toast.LENGTH_SHORT);
                    return;
                }


                //Login and authenticate user
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this.getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (!task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this.getContext(), getString(R.string.auth_failed), Toast.LENGTH_LONG).show();

                                }
                                else {
                                    startActivity(new Intent(LoginActivity.this.getContext(), MainActivity.class));
                                    /**
                                    Fragment fragment = new DashBoard();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.content_login, fragment, TAG);
                                    fragmentTransaction.addToBackStack(TAG);
                                    fragmentTransaction.commit(); */
                                }
                            }
                        });
            }
        });

        return rootView;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            }
        }
    }

    //Login with Google
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            //updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener( this.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this.getContext(), DashBoard.class));
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this.getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }


}