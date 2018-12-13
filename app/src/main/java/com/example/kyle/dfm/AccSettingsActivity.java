package com.example.kyle.dfm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    private Button deleteData;
    private Button finishedAcc;
    private CardView accSettText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_settings);

        logOut = (Button)findViewById(R.id.logOut);
        deleteAcc = (Button)findViewById(R.id.deleteAcc);
        deleteData = (Button)findViewById(R.id.deleteData);
        finishedAcc = (Button)findViewById(R.id.finishedAcc);
        accSettText =  (CardView)findViewById(R.id.accSettText);

        logOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();

                Intent welcome = new Intent(AccSettingsActivity.this,WelcomeActivity.class);

                startActivity(welcome);


            }
        });

        deleteData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


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
