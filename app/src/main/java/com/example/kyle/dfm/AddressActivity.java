package com.example.kyle.dfm;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class AddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
    }


    public String convertAddress(String addresstxt){
        String coordinates = "";


        //NEEDS TO BE DONE


        return coordinates;

    }
    public void saveButtonClicked(View v){
        String address1Text = ((EditText)findViewById(R.id.address1input)).getText().toString();
        String address2Text = ((EditText)findViewById(R.id.address2input)).getText().toString();
        String cityText = ((EditText)findViewById(R.id.cityinput)).getText().toString();
        String stateText = ((EditText)findViewById(R.id.stateinput)).getText().toString();
        String zipcodeText = ((EditText)findViewById(R.id.zipcodeinput)).getText().toString();

        if(address1Text.equals("") || address1Text.length() == 0 || address1Text==null){
            Toast.makeText(AddressActivity.this, "Please enter an item to be saved!", Toast.LENGTH_LONG).show();
        }
        else if(cityText.equals("") || cityText.length() == 0 || cityText==null){
            Toast.makeText(AddressActivity.this, "Please enter an item to be saved!", Toast.LENGTH_LONG).show();
        }
        else if(stateText.equals("") || stateText.length() == 0 || stateText==null){
            Toast.makeText(AddressActivity.this, "Please enter an item to be saved!", Toast.LENGTH_LONG).show();
        }
        else if(zipcodeText.equals("") || zipcodeText.length() == 0 || zipcodeText==null){
            Toast.makeText(AddressActivity.this, "Please enter an item to be saved!", Toast.LENGTH_LONG).show();
        }
        else{
            String addressText = address1Text + "," + address2Text + "," + cityText + "," + stateText + "," + zipcodeText;
            Intent intent = new Intent();
            intent.putExtra(Intent_Constants.INTENT_MESSAGE_FIELD,addressText);
            setResult(Intent_Constants.INTENT_RESULT_CODE,intent);
            AddressData addressData = new AddressData(addressText);
            AddressSource.get(AddressActivity.this).sendAddress(addressData);

            finish();
            //super.onBackPressed();
        }
    }

}
