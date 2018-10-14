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

public class AddressSource {


    public interface AddressListener {
        void onAddressReceived(List<AddressData> items);
    }

    private static AddressSource sItemAddressSource;
    private Context mContext;

    public static AddressSource get(Context context) {
        if (sItemAddressSource == null) {
            sItemAddressSource = new AddressSource(context);
        }
        return sItemAddressSource;
    }

    private AddressSource(Context context) {
        mContext = context;
    }

    //Firebase methods
    public void getAddress(final AddressListener addressListener) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("addressData");
        //DatabaseReference itemsRef = databaseRef.child("addressData");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<AddressData> addressData = new ArrayList<>();
                Iterable<DataSnapshot> iter = dataSnapshot.getChildren();
                for (DataSnapshot itemSnapshot: iter) {
                    //String description = itemSnapshot.child("mDataName").getValue(String.class);
                    AddressData addressData1 = new AddressData(itemSnapshot);
                    addressData.add(addressData1);

                }
                addressListener.onAddressReceived(addressData);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sendAddress(AddressData addressData) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("addressData");
        //DatabaseReference addressRef = databaseRef.child("addressData");
        DatabaseReference newAddressRef = databaseRef.push();

        Map<String, String> addressValMap = new HashMap<>();
        addressValMap.put("mAddressName", addressData.getAddressName());
        newAddressRef.setValue(addressValMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Toast.makeText(mContext, "AddressData has been posted!", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

}
