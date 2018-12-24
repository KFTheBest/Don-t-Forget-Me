package com.example.kyle.dfm;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LocationSettings extends AppCompatActivity {


    private CardView changeLocalText, changeRadiusText;

    private Spinner changeLocal, changeRadius;

    private Button viewLocals;

    private Button finishedLocal;

    ArrayAdapter<String> adapterAddress;

    ArrayAdapter<Integer>adapterRadius;

    ArrayList<String> arrayList;

    List<AddressData> mAddData;

    ArrayList<Integer>radiusArray;

    private int defaultRadius = 50;

    private String defaultAddress;

    SharedPreferences prefAdd;

    ArrayList<String>convertedAddresses;

    private int radPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location_settings);

        changeLocalText = findViewById(R.id.changeLocalText);

        changeRadiusText = findViewById(R.id.changeRadiusText);

        finishedLocal = (Button) findViewById(R.id.finishedLocal);

        viewLocals = (Button) findViewById(R.id.viewLocal);

        changeLocal = (Spinner) findViewById(R.id.changeLocal);

        changeRadius = (Spinner) findViewById(R.id.changeRadius);

        radiusArray = new ArrayList<>();

        radiusArray.add(25);

        radiusArray.add(50);

        radiusArray.add(75);

        radiusArray.add(100);

        prefAdd = PreferenceManager.getDefaultSharedPreferences(this);

        final SharedPreferences.Editor editor = prefAdd.edit();

        editor.putInt("Radius",defaultRadius);

        editor.commit();

        arrayList = new ArrayList<>();

        adapterAddress = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrayList);



        AddressSource.get(LocationSettings.this).getAddress(new AddressSource.AddressListener() {

            @Override
            public void onAddressReceived(List<AddressData> items) {

                mAddData = items;

                changeLocal.setAdapter(new AddressAdapter(LocationSettings.this, R.layout.support_simple_spinner_dropdown_item, items));


                if(!items.isEmpty()) {

                    if(!prefAdd.contains("Address")) {

                        defaultAddress = items.get(0).getAddressName();

                        editor.putString("Address", defaultAddress);

                        editor.commit();
                    }
                    else{
                        defaultAddress = prefAdd.getString("Address", "");
                    }
                }
                else{

                    AlertDialog.Builder adb = new AlertDialog.Builder(LocationSettings.this);

                    adb.setTitle("Alert!");

                    adb.setMessage("There aren't any addresses saved yet! Please add one before continuing!");

                    adb.setNegativeButton("Cancel", null);

                    adb.setPositiveButton("Add an Address!", new AlertDialog.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            Intent register = new Intent(LocationSettings.this,AddressActivity.class);

                            startActivity(register);

                        }});
                    adb.show();
                }
            }
        });

        adapterAddress.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        changeLocal.setAdapter(adapterAddress);

        changeLocal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                AddressData addressData = (AddressData)parent.getItemAtPosition(position);

                defaultAddress = addressData.getAddressName();

                radPos = position;

                editor.remove("Address");

                editor.putString("Address",defaultAddress);

                editor.commit();




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        adapterRadius = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,radiusArray);

        adapterRadius.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        changeRadius.setAdapter(adapterRadius);



        changeRadius.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, long id) {

                defaultRadius = (int) parent.getItemAtPosition(position);



                editor.remove("Radius");

                editor.putInt("Radius", defaultRadius);

                editor.commit();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        viewLocals.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent viewLoc = new Intent(LocationSettings.this,ViewLocal.class);

                startActivity(viewLoc);

            }
        });


        finishedLocal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                LocationSettings.super.onBackPressed();

            }
        });

    }

    @Override
    protected void onStart() {

        super.onStart();

      defaultRadius = prefAdd.getInt("Radius", 50);

      changeRadius.setSelection(radPos);

      defaultAddress = prefAdd.getString("Address", "Error,Error");


    }
}