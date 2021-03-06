package com.example.kyle.dfm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class AccSettingsActivity extends AppCompatActivity {

    private Button logOut;

    private Button deleteAcc;

    private Button linkAcc;

    private Button finishedAcc;

    private CardView accSettText;

    private SharedPreferences prefAcc;

    private GoogleApiClient mGoogleApiClient;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_acc_settings);

        logOut = (Button)findViewById(R.id.logOut);

        deleteAcc = (Button)findViewById(R.id.deleteAcc);

        linkAcc = (Button)findViewById(R.id.linkAcc);

        finishedAcc = (Button)findViewById(R.id.finishedAcc);

        accSettText =  (CardView)findViewById(R.id.accSettText);

        mAuth = FirebaseAuth.getInstance();

        prefAcc = PreferenceManager.getDefaultSharedPreferences(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(AccSettingsActivity.this, "You got an error", Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        logOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>()
                {
                     @Override
                     public void onResult(@NonNull Status status)
                     {
                         mAuth.signOut();

                     }

                 });

                SharedPreferences.Editor editor = prefAcc.edit();

                editor.remove("Logged");

                editor.commit();

                Intent welcome = new Intent(AccSettingsActivity.this,WelcomeActivity.class);

                welcome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(welcome);

                finish();


            }
        });

        linkAcc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent link = new Intent(AccSettingsActivity.this,LinkActivity.class);

                startActivity(link);

            }
        });

        deleteAcc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // Get auth credentials from the user for re-authentication. The example below shows
                // email and password credentials but there are multiple possible providers,
                // such as GoogleAuthProvider or FacebookAuthProvider.
                AuthCredential credentialEmail = EmailAuthProvider
                        .getCredential("user@example.com", "password1234");

                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credentialEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Intent delAccount = new Intent(AccSettingsActivity.this,LoginActivity.class);
                                                    startActivity(delAccount);
                                                }
                                            }
                                        });

                            }
                        });
                AuthCredential credentialGoogle = GoogleAuthProvider
                        .getCredential("user@example.com", "password1234");
                user.reauthenticate(credentialGoogle)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Intent delAccount = new Intent(AccSettingsActivity.this,LoginActivity.class);
                                                    startActivity(delAccount);
                                                }
                                            }
                                        });

                            }
                        });


            }
        });

        finishedAcc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AccSettingsActivity.super.onBackPressed();

            }
        });


    }
}
