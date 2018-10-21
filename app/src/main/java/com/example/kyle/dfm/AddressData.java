package com.example.kyle.dfm;

import com.google.firebase.database.DataSnapshot;

public class AddressData {

    private String maddressName;

    public AddressData(String addressName){
        maddressName = addressName;
    }

    public AddressData(DataSnapshot snapshot){
        maddressName = snapshot.child("mAddressName").getValue(String.class);
    }
    String getAddressName(){
        return maddressName;
    }


}
