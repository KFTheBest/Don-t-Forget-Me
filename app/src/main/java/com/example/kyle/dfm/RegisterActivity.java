package com.example.kyle.dfm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

    public SharedPreferences preferences;



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
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        emailRegInput.setHint("Ex. abc@xys.com");
        passwordRegInput.setHint("Must be more than 6 characters!");





       mRegister.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View view) {


               if(!(emailValid(emailRegInput.getText().toString())) || !(passValid(passwordRegInput.getText().toString()))){
                   Toast.makeText(RegisterActivity.this, "Please enter a valid email address and password!.",
                           Toast.LENGTH_SHORT).show();
               }
               else {
                   createUserWithEmailAndPassword(emailRegInput.getText().toString(), passwordRegInput.getText().toString());
               }

           }
       });

    }

    private boolean emailValid(String emailAddress){
        return (emailAddress.contains("@") || emailAddress.contains(" ") || emailAddress.contains(","));
    }

    private boolean passValid(String password){
        return password.length()>6;
    }


    @Override
    protected void onStart() {

        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }

    public void updateUI(FirebaseUser user) {

    }

    //this is for username and passwork textfields
    public boolean createUserWithEmailAndPassword(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,  new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                // Sign in success, update UI with the signed-in user's information
                if (task.isSuccessful()) {

                    regSuccess = true;

                    final SharedPreferences.Editor editor = preferences.edit();

                    editor.putBoolean("Logged",regSuccess);

                    editor.commit();

                    final FirebaseUser user = mAuth.getCurrentUser();

                    updateUI(user);

                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(RegisterActivity.this, "A verification email was sent to your email!",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "Email verification email unable to be sent. Try again.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                    Intent intent = new Intent(RegisterActivity.this,HomeF.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);

                    finish();

                }
                else {

                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();

                    updateUI(null);

                    regSuccess = false;
                }

            }
        });

        return regSuccess;
    }

}
