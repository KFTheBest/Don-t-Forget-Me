package com.example.kyle.dfm;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewList extends AppCompatActivity {

    ListView listView;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    List<Data> mData;
    String messageText = "";
    int position1 = 0;
    TextView dataName;

    private static final String tag = "ViewList";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);
       listView = (ListView)findViewById(R.id.listView);


       arrayList = new ArrayList<>();
       arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        //listView.setAdapter(arrayAdapter);
        DataSource.get(ViewList.this).getData(new DataSource.DataListener(){
            @Override
            public void onDataReceived(List<Data>data){
                mData = data;
                listView.setAdapter(new DataAdapter(ViewList.this, R.layout.list_view_data,data));
            }
        });


        dataName = (TextView)findViewById(R.id.dataName);


        final Intent intent = new Intent(this,ViewList.class);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){

               /* Object o = listView.getItemAtPosition(position);
                Data data = (Data)o;
                //Toast.makeText(ListOfItemsActivity.this, "You have chosen : " + " " + item.getItemName(), Toast.LENGTH_LONG).show();

                //this will call ItemDetailsActivity
                Intent i = new Intent(listView.getContext(), EditScreen.class);
                i.putExtra("DataObject", data);
                startActivity(i);
                */
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

        if (resultCode == Intent_Constants.INTENT_REQUEST_CODE) {
            messageText = data.getStringExtra(Intent_Constants.INTENT_MESSAGE_FIELD);
            arrayList.add(messageText);
            arrayAdapter.notifyDataSetChanged();


        } else if (resultCode == Intent_Constants.INTENT_REQUEST_CODE_TWO) {
            messageText = data.getStringExtra(Intent_Constants.INTENT_CHANGED_MESSAGE);
            position1 = data.getIntExtra(Intent_Constants.INTENT_ITEM_POSTION, -1);
            arrayList.remove(position1);
            arrayList.add(position1, messageText);
            arrayAdapter.notifyDataSetChanged();
        }
    }


}





