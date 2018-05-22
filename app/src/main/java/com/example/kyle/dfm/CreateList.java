package com.example.kyle.dfm;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateList extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        return inflater.inflate(R.layout.activity_create_list,container,false);
    }

    public void saveButtonClicked(View v){
        String messageText = ((EditText)getView().findViewById(R.id.message)).getText().toString();

        if(messageText.equals("") || messageText.length() == 0 || messageText==null){
            Toast.makeText(getActivity(), "Please enter an item to be saved!", Toast.LENGTH_LONG).show();
        }
        else{
            Intent intent = new Intent();
            intent.putExtra(Intent_Constants.INTENT_MESSAGE_FIELD,messageText);
            getActivity().setResult(Intent_Constants.INTENT_RESULT_CODE,intent);
            Data data = new Data(messageText);
            DataSource.get(getActivity()).sendData(data);

            //finish();
        }
    }



}

