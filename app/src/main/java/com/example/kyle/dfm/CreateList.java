package com.example.kyle.dfm;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class CreateList extends ViewList {
    //private TaskHelper mhelper;
    //private ListView mTaskListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

    }

    public void saveButtonClicked(View v){
        String messageText = ((EditText)findViewById(R.id.message)).getText().toString();

        if(messageText.equals("")){

        }
        else{
            Intent intent = new Intent();
            intent.putExtra(Intent_Constants.INTENT_MESSAGE_FIELD,messageText);
            setResult(Intent_Constants.INTENT_RESULT_CODE,intent);


            //db.execSQL(messageText);
            //editor.putString("waa",messageText);
            //editor.commit();

            finish();
        }
    }

    public void onDestroy(){
        super.onDestroy();
        pref = getApplicationContext().getSharedPreferences("waa", MODE_PRIVATE);
        editor = pref.edit();
        editor.putString("waa",messageText);
        if(editor.commit()){
            System.out.print("yay!");
        }
        else{
            System.out.print("noo!");}
    }
    public void onStop(){
        super.onStop();
        pref = getApplicationContext().getSharedPreferences("waa", MODE_PRIVATE);
        editor = pref.edit();
        editor.putString("waa",messageText);
        if(editor.commit()){
            System.out.print("yay!");
        }
        else{
            System.out.print("noo!");}
    }



}

