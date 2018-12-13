package com.example.kyle.dfm;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewLocal extends AppCompatActivity {

    ListView listView;

    ArrayList<String> arrayList;

    ArrayAdapter<String> arrayAdapter;

    List<AddressData> mData;

    String messageText = "";

    int position1 = 0;

    TextView dataName;

    private static final String tag = "ViewList";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_local);

        listView = (ListView)findViewById(R.id.listLocalView);

        arrayList = new ArrayList<>();

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);


        //listView.setAdapter(arrayAdapter);
        AddressSource.get(ViewLocal.this).getAddress(new AddressSource.AddressListener(){
            @Override
            public void onAddressReceived(List<AddressData>items){
                mData = items;
                listView.setAdapter(new AddressAdapter(ViewLocal.this, R.layout.list_view_data,items));
            }
        });


        dataName = (TextView)findViewById(R.id.dataName);


        final Intent intent = new Intent(this,ViewLocal.class);

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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                AlertDialog.Builder adb = new AlertDialog.Builder(ViewLocal.this);

                adb.setTitle("Delete?");

                adb.setMessage("Are you sure you want to delete " + mData.get(i).getAddressName()+ " ?");

                final int positionToRemove = i;

                adb.setNegativeButton("Cancel", null);

                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                        Query itemQuery = ref.child("addressData").orderByChild("mAddressName").equalTo(mData.get(positionToRemove).getAddressName());

                        itemQuery.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {

                                    itemSnapshot.getRef().removeValue();

                                    arrayAdapter.notifyDataSetChanged();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }});
                adb.show();

                return false;
            }
        });

    }

    public void onClick(View v) {

        Intent intent = new Intent();

        intent.setClass(ViewLocal.this, AddressActivity.class);

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





