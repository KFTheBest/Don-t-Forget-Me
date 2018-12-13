package com.example.kyle.dfm;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;



public class CreateList extends AppCompatActivity {
    TextInputLayout textInputLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);
        textInputLayout = findViewById(R.id.createListInput);


    }

    public void saveButtonClicked(View v){
        String messageText = textInputLayout.getEditText().getText().toString();
        if(messageText.equals("") || messageText.length() == 0 || messageText==null){
            Toast.makeText(CreateList.this, "Please enter an item to be saved!", Toast.LENGTH_LONG).show();
        }
        else{
            Intent intent = new Intent();
            intent.putExtra(Intent_Constants.INTENT_MESSAGE_FIELD,messageText);
            setResult(Intent_Constants.INTENT_RESULT_CODE,intent);
            Data data = new Data(messageText);
            DataSource.get(CreateList.this).sendData(data);

            finish();
            super.onBackPressed();
        }
    }



}

