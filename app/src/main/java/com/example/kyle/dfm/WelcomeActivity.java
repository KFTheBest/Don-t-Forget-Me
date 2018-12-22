package com.example.kyle.dfm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    private Button welcomeReg;

    private Button welcomeLog;

    private TextView welcomeText;

    private ImageView welcomeImage;

    public SharedPreferences prefLog;

    private Boolean logged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);


        prefLog = PreferenceManager.getDefaultSharedPreferences(this);

        logged = prefLog.getBoolean("Logged",false);

        if(logged){
            Intent intent = new Intent(WelcomeActivity.this, HomeF.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);

            finish();
        }

        welcomeReg = (Button)findViewById(R.id.welcomeRegister);

        welcomeLog = (Button)findViewById(R.id.welcomeLog);

        welcomeText = (TextView) findViewById(R.id.welcomeText);

        welcomeImage = (ImageView) findViewById(R.id.welcomeImage);

        welcomeReg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent register = new Intent(WelcomeActivity.this,RegisterActivity.class);

                startActivity(register);

            }
        });

        welcomeLog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent logIn = new Intent(WelcomeActivity.this,LoginActivity.class);

                startActivity(logIn);

            }
        });
    }
}
