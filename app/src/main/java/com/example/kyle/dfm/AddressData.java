package com.example.kyle.dfm;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

public class AddressData {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String iD = user.getUid();

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
