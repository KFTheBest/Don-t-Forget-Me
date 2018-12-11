package com.example.kyle.dfm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LocationSettings extends AppCompatActivity {


    private TextView changeLocalText, changeRadiusText;
    private Spinner changeLocal, changeRadius;
    private Button finishedLocal;
    ArrayAdapter<String> adapterAddress;
    ArrayAdapter<Integer>adapterRadius;
    ArrayList<String> arrayList;
    List<AddressData> mAddData;
    ArrayList<Integer>radiusArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_settings);

        changeLocalText = (TextView) findViewById(R.id.changeLocalText);
        changeRadiusText = (TextView) findViewById(R.id.changeRadiusText);
        finishedLocal = (Button) findViewById(R.id.finishedLocal);
        changeLocal = (Spinner) findViewById(R.id.changeLocal);
        changeRadius = (Spinner) findViewById(R.id.changeRadius);

        radiusArray = new ArrayList<>();
        radiusArray.add(25);
        radiusArray.add(50);
        radiusArray.add(75);
        radiusArray.add(100);


        adapterAddress = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrayList);

        AddressSource.get(LocationSettings.this).getAddress(new AddressSource.AddressListener() {
            @Override
            public void onAddressReceived(List<AddressData> items) {

                mAddData = items;

                changeLocal.setAdapter(new AddressAdapter(LocationSettings.this, R.layout.support_simple_spinner_dropdown_item, items));

            }
        });

        adapterAddress.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        changeLocal.setAdapter(adapterAddress);

        changeLocal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });





        finishedLocal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                LocationSettings.super.onBackPressed();

            }
        });
    }
}