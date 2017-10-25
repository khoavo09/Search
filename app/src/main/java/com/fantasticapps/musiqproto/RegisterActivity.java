package com.fantasticapps.musiqproto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * View: Text field field for email, password, and to re-enter password, and button to register
 * Functionality: With the data in the fields, user information is added to the database, and user is registered
 */
public class RegisterActivity extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private final String TAG = "Registration";

    private Button btnRegister;
    private EditText inputEmail, inputPassword, inputPasswordConfirm;
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //intalize page view for register
        View rootView = inflater.inflate(R.layout.activity_register, container, false);

        Log.d("mytag", "Register Page Loaded!");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("mytag", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("mytag", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        btnRegister = (Button) rootView.findViewById(R.id.buttonConfirmRegister);
        inputEmail = (EditText) rootView.findViewById(R.id.registerEmailText);
        inputPassword = (EditText) rootView.findViewById(R.id.registerPasswordText);
        inputPasswordConfirm = (EditText) rootView.findViewById(R.id.registerPasswordConfirmText);

        /**
         * When User clicks register, all the fields r checked
         * If any of the fields are invalid or empty, user is prompted to fix the field
         * If all the fields are valide, user data is inputed to database, and user is taken to dashboard screen
         */
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                String passwordConfirm = inputPasswordConfirm.getText().toString();

                boolean cancel = false;
                View focusView = null;

                inputEmail.setError(null);
                inputPassword.setError(null);
                inputPasswordConfirm.setError(null);


                //Check for invalid fields
                if(TextUtils.isEmpty(email)) {
                    inputEmail.setError(getString(R.string.error_field_required));
                    focusView = inputEmail;
                    cancel = true;
                } else if(!email.contains("@")) {
                    inputEmail.setError(getString(R.string.error_invalid_email));
                    focusView = inputEmail;
                    cancel = true;
                }

                if(TextUtils.isEmpty(password)) {
                    inputPassword.setError(getString(R.string.error_field_required));
                    focusView = inputPassword;
                    cancel = true;
                } else if(password.length() < 6) {
                    inputPassword.setError(getString(R.string.error_invalid_password));
                    focusView = inputPassword;
                    cancel = true;
                }

                if(TextUtils.isEmpty(passwordConfirm)) {
                    inputPasswordConfirm.setError(getString(R.string.error_field_required));
                    focusView = inputPasswordConfirm;
                    cancel = true;
                }

                if(!password.equals(passwordConfirm)) {
                    inputPasswordConfirm.setError("Passwords do not match");
                    focusView = inputPasswordConfirm;
                    cancel = true;
                }

                if(cancel) {
                    focusView.requestFocus();
                    return;
                }

                /** the "dialog" tags are causing the app to crash FYI
                 pDialog.setMessage("Registering ...");
                 showDialog();
                 **/

                //Attempt to make new user
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this.getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                //Registration unsucessful, error message is shown
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this.getContext(), "Unable to Register:" + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                }
                                //If registration successful, user taken to dashboard
                                else {
                                    //hideDialog();
                                    Toast.makeText(RegisterActivity.this.getContext(), "Registration Successful" , Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this.getContext(), MainActivity.class));
                                    /**
                                    Fragment fragment = new DashBoard();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.content_register, fragment, TAG);
                                    fragmentTransaction.addToBackStack(TAG);
                                    fragmentTransaction.commit();*/
                                    //finish();
                                }

                                // ...
                            }
                        });
            }
        });

        return rootView;


    }

    //currently not used
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    //currenly not used
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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
