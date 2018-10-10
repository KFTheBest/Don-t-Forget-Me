package com.example.kyle.dfm;

import com.google.firebase.database.DataSnapshot;

public class AddressData {

    String addressName;

    public AddressData(String addressName){
        this.addressName = addressName;
    }

    public AddressData(DataSnapshot snapshot){
        addressName = snapshot.child("mAddressName").getValue(String.class);
    }
    String getAddressName(){
        return addressName;
    }


}
