package com.example.kyle.dfm;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kyle Frederick on 8/21/2017.
 */

public class DataSource {
        //private ImageLoader mImageLoader;

        public interface DataListener {
            void onDataReceived(List<Data> items);
        }

        private static DataSource sItemDataSource;
        private Context mContext;

        public static DataSource get(Context context) {
            if (sItemDataSource == null) {
                sItemDataSource = new DataSource(context);
            }
            return sItemDataSource;
        }

        private DataSource(Context context) {
            mContext = context;
        }

        //Firebase methods
        public void getData(final DataListener dataListener) {
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference itemsRef = databaseRef.child("data");

            itemsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Data> data = new ArrayList<>();
                    Iterable<DataSnapshot> iter = dataSnapshot.getChildren();
                    for (DataSnapshot itemSnapshot: iter) {
                        //String description = itemSnapshot.child("mDataName").getValue(String.class);
                        Data data1 = new Data(itemSnapshot);
                        data.add(data1);
                    }
                    dataListener.onDataReceived(data);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void sendData(Data data) {
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference dataRef = databaseRef.child("data");
            DatabaseReference newDataRef = dataRef.push();

            Map<String, String> dataValMap = new HashMap<>();
            dataValMap.put("mDataName", data.getDataName());
            newDataRef.setValue(dataValMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        Toast.makeText(mContext, "Item has been posted!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(mContext, ViewList.class);
                        mContext.startActivity(i);
                    }
                }
            });
        }
       // public ImageLoader getImageLoader() {return mImageLoader;
        }






