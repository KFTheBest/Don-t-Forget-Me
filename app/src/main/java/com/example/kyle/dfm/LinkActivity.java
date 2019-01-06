package com.example.kyle.dfm;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;

import java.util.List;

import bolts.Capture;

public class LinkActivity extends AppCompatActivity {

    private Button liGoogle;

    private Button liFacebook;

    private Button liEmail;

    private Button linkDone;

    private FirebaseAuth mAuth;

    private AuthCredential facebookCred;

    private AuthCredential googleCred;

    private AuthCredential emailCred;

    private Boolean googleBool = false;

    private Boolean facebookBool = false;

    private Boolean emailBool = false;

    private GoogleApiClient mGoogleApiClient;

    private CallbackManager callbackManager;

    private static final int RC_SIGN_IN = 1;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_link);

        liGoogle = (Button) findViewById(R.id.liGoogle);

        liFacebook = (Button) findViewById(R.id.liFacebook);

        linkDone = (Button) findViewById(R.id.linkDone);

        liEmail = (Button) findViewById(R.id.liEmail);

        mAuth = FirebaseAuth.getInstance();

        liGoogle.setText("Link account with Google");

        liFacebook.setText("Link account with Facebook");

        liEmail.setText("Link account with Email");

        callbackManager = CallbackManager.Factory.create();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LinkActivity.this, "You got an error", Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null ) {
                    // User is signed in



                } else {
                    // User is signed out

                }
                // ...
            }
        };


        liGoogle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

               /* if (!googleBool) {
                    liGoogle.setText("Unlink account with Google");
                    googleBool = true;

                    final String[] providers = {"Email", "Facebook"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(LinkActivity.this);
                    builder.setTitle("Which provider do you use to login?");
                    builder.setItems(providers, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // the user clicked on colors[which]
                            if (providers[which] == "Email") {

                                LayoutInflater factory = LayoutInflater.from(LinkActivity.this);
                                final View textEntryView = factory.inflate(R.layout.dialoglayout, null);
                                final EditText input1 = (EditText) textEntryView.findViewById(R.id.dialogEmail);
                                final EditText input2 = (EditText) textEntryView.findViewById(R.id.dialogPassword);
                                input1.setText("DefaultValue", TextView.BufferType.EDITABLE);
                                input2.setText("DefaultValue", TextView.BufferType.EDITABLE);
                                final AlertDialog.Builder alert = new AlertDialog.Builder(LinkActivity.this);
                                alert.setTitle("Alert").setMessage("Please enter" + " your credentials!")
                                        .setView(textEntryView).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        mAuth.signInWithEmailAndPassword(input1.getText().toString(), input2.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    emailCred = EmailAuthProvider.getCredential(input1.getText().toString(), input2.getText().toString());
                                                } else {
                                                    Toast.makeText(LinkActivity.this, "Unable to log In. Please try again.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }).setNegativeButton("Cancel", null);
                                alert.show();


                                mAuth.getCurrentUser().linkWithCredential(emailCred)
                                        .addOnCompleteListener(LinkActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {

                                                    FirebaseUser user = task.getResult().getUser();
                                                    Toast.makeText(LinkActivity.this, "Your email account is now linked with your Google" +
                                                            " account!", Toast.LENGTH_SHORT).show();
                                                } else {

                                                    Toast.makeText(LinkActivity.this, "Link was unsuccessful. Please try again.",
                                                            Toast.LENGTH_SHORT).show();

                                                }

                                                // ...
                                            }
                                        });

                            } else {

                                LoginManager.getInstance().registerCallback(callbackManager,
                                        new FacebookCallback<LoginResult>() {
                                            @Override
                                            public void onSuccess(LoginResult loginResult) {
                                                facebookCred = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                                            }

                                            @Override
                                            public void onCancel() {
                                                // App code
                                            }

                                            @Override
                                            public void onError(FacebookException exception) {
                                                // App code
                                            }
                                        });


                                mAuth.getCurrentUser().linkWithCredential(facebookCred)
                                        .addOnCompleteListener(LinkActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {

                                                    FirebaseUser user = task.getResult().getUser();
                                                    Toast.makeText(LinkActivity.this, "Your Facebook account is now linked with your Google" +
                                                            " account!", Toast.LENGTH_SHORT).show();
                                                } else {

                                                    Toast.makeText(LinkActivity.this, "Link was unsuccessful. Please try again.",
                                                            Toast.LENGTH_SHORT).show();

                                                }

                                                // ...
                                            }
                                        });

                            }
                        }
                    });
                    builder.show();
                } else {


                    List<? extends UserInfo> infos = mAuth.getCurrentUser().getProviderData();

                    String providerId = "";

                    for (UserInfo userInfo : infos) {
                        if (userInfo.getProviderId() == "google.com") {
                            providerId = userInfo.getProviderId();
                        }
                    }


                    mAuth.getCurrentUser().unlink(providerId)
                            .addOnCompleteListener(LinkActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LinkActivity.this, "Your Google account is now unlinked!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    liGoogle.setText("Link account with Google");
                    googleBool = false;

                }*/


            }
        });

        liFacebook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                /*if (!facebookBool) {
                    liFacebook.setText("Unlink account with Facebook");
                    facebookBool = true;

                    final String[] providers = {"Email", "Google"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(LinkActivity.this);
                    builder.setTitle("Which provider do you use to login?");
                    builder.setItems(providers, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // the user clicked on colors[which]
                            if (providers[which] == "Email") {

                                LayoutInflater factory = LayoutInflater.from(LinkActivity.this);
                                final View textEntryView = factory.inflate(R.layout.dialoglayout, null);
                                final EditText input1 = (EditText) textEntryView.findViewById(R.id.dialogEmail);
                                final EditText input2 = (EditText) textEntryView.findViewById(R.id.dialogPassword);


                                final AlertDialog.Builder alert = new AlertDialog.Builder(LinkActivity.this);
                                alert.setTitle("Alert").setMessage("Please enter" + " your credentials!")
                                        .setView(textEntryView).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        mAuth.signInWithEmailAndPassword(input1.getText().toString(), input2.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    emailCred = EmailAuthProvider.getCredential(input1.getText().toString(), input2.getText().toString());

                                                } else {
                                                    Toast.makeText(LinkActivity.this, "Unable to log In. Please try again.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }).setNegativeButton("Cancel", null);
                                alert.show();

                                if(emailCred !=null) {


                                    mAuth.getCurrentUser().linkWithCredential(emailCred)
                                            .addOnCompleteListener(LinkActivity.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {

                                                        FirebaseUser user = task.getResult().getUser();
                                                        Toast.makeText(LinkActivity.this, "Your email account is now linked with your Facebook" +
                                                                " account!", Toast.LENGTH_SHORT).show();
                                                    } else {

                                                        Toast.makeText(LinkActivity.this, "Link was unsuccessful. Please try again.",
                                                                Toast.LENGTH_SHORT).show();

                                                    }

                                                    // ...
                                                }
                                            });
                                }else{

                                }

                            } else {



                                mAuth.getAccessToken(true).addOnCompleteListener(LinkActivity.this, new OnCompleteListener<GetTokenResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                                        if(task.isSuccessful()){
                                            googleCred = GoogleAuthProvider.getCredential(task.getResult().getToken(),null);
                                        }
                                    }
                                });


                                if(googleCred != null) {


                                    mAuth.getCurrentUser().linkWithCredential(googleCred)
                                            .addOnCompleteListener(LinkActivity.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {

                                                        FirebaseUser user = task.getResult().getUser();
                                                        Toast.makeText(LinkActivity.this, "Your Google account is now linked with your Facebook" +
                                                                " account!", Toast.LENGTH_SHORT).show();
                                                    } else {

                                                        Toast.makeText(LinkActivity.this, "Link was unsuccessful. Please try again.",
                                                                Toast.LENGTH_SHORT).show();

                                                    }

                                                    // ...
                                                }
                                            });
                                }else{
                                    Toast.makeText(LinkActivity.this, "Seems you didn't log in with Google. Please use the provider you logged in with.",
                                            Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                    });
                    builder.show();
                } else {
                    List<? extends UserInfo> infos = mAuth.getCurrentUser().getProviderData();

                    String providerId = "";

                    for (UserInfo userInfo : infos) {
                        if (userInfo.getProviderId() == "facebook.com") {
                            providerId = userInfo.getProviderId();
                        }
                    }


                    mAuth.getCurrentUser().unlink(providerId)
                            .addOnCompleteListener(LinkActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LinkActivity.this, "Your Facebook account is now unlinked!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    liFacebook.setText("Link account with Facebook");
                    facebookBool = false;

                }*/



                //Log in with face book then take that credential and link with the current one


            }
        });


        liEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

               /* LayoutInflater factory = LayoutInflater.from(LinkActivity.this);
                final View textEntryView = factory.inflate(R.layout.dialoglayout, null);
                final EditText input1 = (EditText) textEntryView.findViewById(R.id.dialogEmail);
                final EditText input2 = (EditText) textEntryView.findViewById(R.id.dialogPassword);
                input1.setText("DefaultValue", TextView.BufferType.EDITABLE);
                input2.setText("DefaultValue", TextView.BufferType.EDITABLE);
                final AlertDialog.Builder alert = new AlertDialog.Builder(LinkActivity.this);
                alert.setTitle("Alert").setMessage("Please enter" + " your credentials!")
                        .setView(textEntryView).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mAuth.signInWithEmailAndPassword(input1.getText().toString(), input2.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    emailCred = EmailAuthProvider.getCredential(input1.getText().toString(), input2.getText().toString());
                                } else {
                                    Toast.makeText(LinkActivity.this, "Unable to log In. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).setNegativeButton("Cancel", null);
                alert.show();

                if (!emailBool) {

                    liEmail.setText("Unlink account with Email");
                    emailBool = true;

                    final String[] providers = {"Facebook", "Google"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(LinkActivity.this);
                    builder.setTitle("Which provider do you use to login?");
                    builder.setItems(providers, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // the user clicked on colors[which]
                            if (providers[which] == "Facebook") {


                                mAuth.getCurrentUser().linkWithCredential(facebookCred)
                                        .addOnCompleteListener(LinkActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {

                                                    FirebaseUser user = task.getResult().getUser();
                                                    Toast.makeText(LinkActivity.this, "Your Facebook account is now linked with your Email" +
                                                            " account!", Toast.LENGTH_SHORT).show();
                                                } else {

                                                    Toast.makeText(LinkActivity.this, "Link was unsuccessful. Please try again.",
                                                            Toast.LENGTH_SHORT).show();

                                                }

                                                // ...
                                            }
                                        });

                            } else {

                                mAuth.getAccessToken(true).addOnCompleteListener(LinkActivity.this, new OnCompleteListener<GetTokenResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                                        if(task.isSuccessful()){
                                            googleCred = GoogleAuthProvider.getCredential(task.getResult().getToken(),null);
                                        }
                                    }
                                });

                                if(googleCred != null) {

                                    mAuth.getCurrentUser().linkWithCredential(googleCred)
                                            .addOnCompleteListener(LinkActivity.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {

                                                        FirebaseUser user = task.getResult().getUser();
                                                        Toast.makeText(LinkActivity.this, "Your Google account is now linked with your Email" +
                                                                " account!", Toast.LENGTH_SHORT).show();
                                                    } else {

                                                        Toast.makeText(LinkActivity.this, "Link was unsuccessful. Please try again.",
                                                                Toast.LENGTH_SHORT).show();

                                                    }

                                                    // ...
                                                }
                                            });

                                }else{
                                    Toast.makeText(LinkActivity.this, "Seems you didn't log in with Google. Please use the provider you logged in with.",
                                            Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                    });
                    builder.show();

                } else {

                    List<? extends UserInfo> infos = mAuth.getCurrentUser().getProviderData();

                    String providerId = "";

                    for (UserInfo userInfo : infos) {
                        if (userInfo.getProviderId() == "password") {
                            providerId = userInfo.getProviderId();
                        }
                    }


                    mAuth.getCurrentUser().unlink(providerId)
                            .addOnCompleteListener(LinkActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LinkActivity.this, "Your email account is now unlinked!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    liEmail.setText("Link account with Email");
                    emailBool = false;


                }*/


            }
        });

        linkDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                LinkActivity.super.onBackPressed();

            }
        });

    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
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

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        googleCred = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(googleCred)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();


                        } else {
                            // If sign in fails, display a message to the user.

                        }
                    }
                });
    }
}

