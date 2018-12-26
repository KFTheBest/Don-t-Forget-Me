package com.example.kyle.dfm;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private final String TAG = "auth";

    private SignInButton mGoogleBtn;

    private static final int RC_SIGN_IN = 1;

    private GoogleApiClient mGoogleApiClient;

    private Button msignIn;

    private Button forgotButton;

    private Button fbSignIn;

    private TextView emailAddressText;

    private TextView passwordText;

    private EditText passwordInput;

    private EditText emailAddressInput;

    private boolean logSuccess;

    public SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        msignIn = (Button) findViewById(R.id.signin);

        mGoogleBtn = (SignInButton) findViewById(R.id.googleBtn);

        emailAddressInput = (EditText)findViewById(R.id.emailAddressInput);

        emailAddressText = (TextView)findViewById(R.id.emailAddressText);

        passwordText = (TextView)findViewById(R.id.passwordText);

        passwordInput = (EditText)findViewById(R.id.passwordInput);

        logSuccess = false;

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        final SharedPreferences.Editor editor = preferences.edit();

        forgotButton = (Button)findViewById(R.id.forgotPass);

        fbSignIn = (Button)findViewById(R.id.fbLogin);


        //new code
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this, "You got an error", Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();

                editor.putBoolean("Logged",logSuccess = true);
                editor.commit();


            }
        });

        //this is for username and passwork textfields
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                boolean log = preferences.getBoolean("Logged", false);

                if (user != null && log) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent logIntent = new Intent(LoginActivity.this,HomeF.class);

                    logIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(logIntent);

                    finish();


                } else {
                    // User is signed out
                    Toast.makeText(getApplicationContext(), "Log out successful!", Toast.LENGTH_SHORT);
                }
                // ...
            }
        };


        msignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signInWithEmailAndPassword(emailAddressInput.getText().toString(), passwordInput.getText().toString());

            }
        });
        //this is for username and passwork textfields


        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Send Instructions to Email");
                builder.setMessage("Please enter the email you used to register to send the rest instructions to!");

// Set up the input
                final EditText input = new EditText(LoginActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        final String emailAddress = input.getText().toString();



                        mAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Log.d("Sent", "We out dis bih");

                                    Toast.makeText(LoginActivity.this, "Email sent!", Toast.LENGTH_SHORT).show();


                                }
                                else {
                                    Toast.makeText(LoginActivity.this, "Email not sent. Try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();



            }
        });

        fbSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

    }


    //new method
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    //this is for username and passwork textfields
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }


        @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    //this is for username and passwork textfields
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //this is for username and passwork textfields
    public boolean signInWithEmailAndPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (task.isSuccessful()) {

                                    logSuccess = true;
                                    Toast.makeText(LoginActivity.this, "Sign in Successful!",
                                            Toast.LENGTH_SHORT).show();

                                    SharedPreferences.Editor editor = preferences.edit();

                                    editor.putBoolean("Logged",logSuccess = true);
                                    editor.commit();





                                    Intent logIntent = new Intent(LoginActivity.this,HomeF.class);

                                    logIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                    startActivity(logIntent);



                                    finish();

                                } else {


                                    logSuccess = false;
                                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                                    Toast.makeText(LoginActivity.this, "Sign in Failed!",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
        return logSuccess;
    }



    //this is for username and passwork textfields


    //this is for username and passwork textfields
    public void updateUI(FirebaseUser user) {

    }
}




