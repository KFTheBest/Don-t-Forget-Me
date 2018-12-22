package com.example.kyle.dfm;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final String iD = user.getUid();

        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference(iD).child("addressData");

        //DatabaseReference itemsRef = databaseRef.child("addressData");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> iter = dataSnapshot.getChildren();
                List<AddressData> addressData = new ArrayList<>();
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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String iD = user.getUid();

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference(iD).child("addressData");
        DatabaseReference addressRef = databaseRef.push();

        Map<String, Object> addressValMap = new HashMap<>();
        addressValMap.put("mAddressName", addressData.getAddressName());
        addressRef.setValue(addressValMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Toast.makeText(mContext, "Address Data has been posted!", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

}
