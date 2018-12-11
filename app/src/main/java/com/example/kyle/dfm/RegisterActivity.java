package com.example.kyle.dfm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private Button mRegister;
    private TextView emailRegText;
    private TextView passwordRegText;
    private EditText emailRegInput;
    private EditText passwordRegInput;
    private FirebaseAuth mAuth;
    private boolean regSuccess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


       mRegister = (Button)findViewById(R.id.register);
       emailRegText = (TextView) findViewById(R.id.emailRegText);
       passwordRegText = (TextView) findViewById(R.id.passwordRegText);
       emailRegInput = (EditText) findViewById(R.id.emailRegInput);
       passwordRegInput = (EditText) findViewById(R.id.passwordRegInput);
       mAuth = FirebaseAuth.getInstance();
       regSuccess = false;

       mRegister.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View view) {

               createUserWithEmailAndPassword(emailRegInput.getText().toString(),passwordRegInput.getText().toString());

               if(regSuccess){
                   Intent home = new Intent(RegisterActivity.this,HomeF.class);

                   startActivity(home);
               }
           }
       });

    }

    //this is for username and passwork textfields
    public void createUserWithEmailAndPassword(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                // Sign in success, update UI with the signed-in user's information
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Login is successful",
                            Toast.LENGTH_SHORT).show();

                    FirebaseUser user = mAuth.getCurrentUser();
                    //updateUI(user);
                    regSuccess = true;
                }
                else {

                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    //updateUI(null);
                    regSuccess = false;
                }
                // ...
            }
        });
    }

}
