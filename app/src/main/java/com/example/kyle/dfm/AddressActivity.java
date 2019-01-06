package com.example.kyle.dfm;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddressActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener{

    String address1Text;

    TextInputLayout address1Lay;

    String address2Text;

    TextInputLayout address2Lay;

    String cityText;

    TextInputLayout addressCityLay;

    String stateText;

    TextInputLayout addressStateLay;

    String zipcodeText;

    TextInputLayout addressZipLay;

    private static final int MY_PERMISSION_REQUEST_CODE = 7171;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 7172;

    private Button saveCoordinates;

    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;


    private GoogleApiClient mGoogleApiClient;

    public Location mLastLocation;

    private static int UPDATE_INTERVAL = 5000; // SEC

    private static int FATEST_INTERVAL = 3000; // SEC

    private static int DISPLACEMENT = 10; // METERS

    double latitude;

    double longitude;

    public String usrLocation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_address);

        buildGoogleApiClient();

        saveCoordinates = (Button) findViewById(R.id.autoBtn);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this, new String[]{

                    android.Manifest.permission.ACCESS_FINE_LOCATION,

                    Manifest.permission.ACCESS_COARSE_LOCATION

            }, MY_PERMISSION_REQUEST_CODE);
        } else {

            if (checkPlayServices()) {

                buildGoogleApiClient();

                createLocationRequest();
            }
        }
        saveCoordinates.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                saveLocation();
            }
        });

        address1Lay = findViewById(R.id.address1input);
        address2Lay = findViewById(R.id.address2input);
        addressCityLay = findViewById(R.id.addressCityInput);
        addressStateLay = findViewById(R.id.addressStateInput);
        addressZipLay = findViewById(R.id.addressZipInput);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case MY_PERMISSION_REQUEST_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (checkPlayServices()) {

                        createLocationRequest();
                    }
                }
                break;
        }
    }

    public LatLng convertAddress(Context context,String addresstxt){

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(addresstxt, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;

    }

    @Override
    protected void onResume() {

        super.onResume();

        checkPlayServices();
    }

    @Override
    protected void onStart() {

        super.onStart();

        if(mGoogleApiClient != null)

            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);

        if(mGoogleApiClient != null)

            mGoogleApiClient.disconnect();

        super.onStop();
    }

    private void saveLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED

                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {

            latitude = mLastLocation.getLatitude();

            longitude = mLastLocation.getLongitude();

            String addressText = Double.toString(latitude) + "," + Double.toString(longitude);

            AddressData addressData = new AddressData(addressText);

            Log.d("Save1",addressData.toString());

            AddressSource.get(getApplicationContext()).sendAddress(addressData);

            Log.d("Save2",addressData.toString());

            finish();

            super.onBackPressed();

        } else{}

    }

    private void createLocationRequest() {

        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL);

        mLocationRequest.setFastestInterval(FATEST_INTERVAL);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)

                .addConnectionCallbacks(this)

                .addOnConnectionFailedListener(this)

                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();
    }

    private boolean checkPlayServices() {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {

            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {

                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();

            } else {

                Toast.makeText(getApplicationContext(), "This device is not supported", Toast.LENGTH_LONG).show();

                finish();
            }
            return false;
        }
        return true;
    }

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdates() {

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if(mRequestingLocationUpdates) {

            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {

        if(location != mLastLocation){

            mLastLocation = location;

        }
        else{

            latitude = mLastLocation.getLatitude();

            longitude = mLastLocation.getLongitude();
        }

    }

    public void saveButtonClicked(View v){

        address1Text = address1Lay.getEditText().getText().toString();

        address2Text = address2Lay.getEditText().getText().toString();

        cityText = addressCityLay.getEditText().getText().toString();

        stateText = addressStateLay.getEditText().getText().toString();

        zipcodeText = addressZipLay.getEditText().getText().toString();

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

            LatLng coordinates = convertAddress(getApplicationContext(),addressText);

            addressText = Double.toString(coordinates.latitude) + "," + Double.toString(coordinates.longitude);

            AddressData addressData = new AddressData(addressText);

            AddressSource.get(AddressActivity.this).sendAddress(addressData);

            finish();

            super.onBackPressed();
        }
    }
}
