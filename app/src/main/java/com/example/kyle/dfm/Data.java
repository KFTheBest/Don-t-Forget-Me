package com.example.kyle.dfm;

/**
 * Created by Kyle on 4/2/2017.
 */

import android.provider.BaseColumns;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;


public class Data implements Serializable {

    String dataName;

    public Data(String dataName){
        this.dataName = dataName;
    }

    public Data(DataSnapshot snapshot){
        dataName = snapshot.child("mDataName").getValue(String.class);
    }
    String getDataName(){
        return dataName;
    }


}
