package com.example.kyle.dfm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.kyle.dfm.Task;
import com.example.kyle.dfm.TaskHelper;
import java.util.ArrayList;
import java.util.List;

public class ViewList extends AppCompatActivity {

    RelativeLayout listView = (RelativeLayout)findViewById(R.id.activity_view_list);
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    String messageText = "";
    int position1 = 0;
//    SQLiteDatabase db = openOrCreateDatabase("db",MODE_PRIVATE, null);


    SharedPreferences pref = null;
    SharedPreferences.Editor editor = null;

    private static final String tag = "ViewList";
    //private TaskHelper mhelper = new TaskHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);


        //pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        //editor = pref.edit();
       listView = (RelativeLayout) findViewById(R.id.activity_view_list);


        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
        //messageText = pref.getString("waa",null);

        final Intent intent = new Intent(this,ViewList.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){

                    Intent intent2 = intent;
                    intent.setClass(ViewList.this, EditScreen.class);
                    intent.putExtra(Intent_Constants.INTENT_MESSAGE_DATA, arrayList.get(position).toString());
                    intent.putExtra(Intent_Constants.INTENT_ITEM_POSTION, position);
                    startActivityForResult(intent, Intent_Constants.INTENT_REQUEST_CODE_TWO);
                }
            });


    }


    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(ViewList.this, CreateList.class);
        startActivityForResult(intent, Intent_Constants.INTENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Cursor data2 = db.rawQuery("idk", null);
        //data2.moveToFirst();
        SharedPreferences pref;

        SharedPreferences.Editor editor;
        pref = getApplicationContext().getSharedPreferences("waa", MODE_PRIVATE);
        editor = pref.edit();


        if (resultCode == Intent_Constants.INTENT_REQUEST_CODE) {
            messageText = data.getStringExtra(Intent_Constants.INTENT_MESSAGE_FIELD);
            arrayList.add(messageText);
            arrayAdapter.notifyDataSetChanged();
            editor.putString("waa",messageText);
            editor.commit();


            /*messageText = data2.getString(0);
            arrayList.add(messageText);
            arrayAdapter.notifyDataSetChanged();
            */
        } else if (resultCode == Intent_Constants.INTENT_REQUEST_CODE_TWO) {
            messageText = data.getStringExtra(Intent_Constants.INTENT_CHANGED_MESSAGE);
            //messageText = data2.getString(0);
            position1 = data.getIntExtra(Intent_Constants.INTENT_ITEM_POSTION, -1);
            arrayList.remove(position1);
            arrayList.add(position1, messageText);
            arrayAdapter.notifyDataSetChanged();
            editor.putString("waa",messageText);
            editor.commit();
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        SharedPreferences pref;

        SharedPreferences.Editor editor;
        pref = getApplicationContext().getSharedPreferences("waa", MODE_PRIVATE);
        editor = pref.edit();
        editor.putString("waa",messageText);
        editor.commit();
        if(editor.commit()){
            System.out.print("yay!");
        }
        else{
            System.out.print("noo!");}

    }
    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences pref;

        SharedPreferences.Editor editor;
        pref = getApplicationContext().getSharedPreferences("waa", MODE_PRIVATE);
        editor = pref.edit();
        editor.putString("waa",messageText);
        editor.commit();
        if(editor.commit()){
            System.out.print("yay!");
        }
        else{
            System.out.print("noo!");}

    }
    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences pref;

        SharedPreferences.Editor editor;
        pref = getApplicationContext().getSharedPreferences("waa",MODE_PRIVATE);
        messageText = pref.getString("waa",null);
        arrayList.add(messageText);
        arrayAdapter.notifyDataSetChanged();
    }
    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences pref;

        SharedPreferences.Editor editor;
        pref = getApplicationContext().getSharedPreferences("waa", MODE_PRIVATE);
        editor = pref.edit();
        editor.putString("waa", messageText);
        editor.commit();
        if (editor.commit()) {
            System.out.print("yay!");
        } else {
            System.out.print("noo!");
        }
    }

}





