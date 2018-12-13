package com.example.kyle.dfm;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class HomeF extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private android.widget.TextView userMessage; //Good day message to user

    private android.widget.TextView itemCheck; // Message to remind user about items

    ListView listView;

    ArrayList<String> arrayList;

    ArrayAdapter<String> arrayAdapter;

    List<Data> mData;

    TextView dataName;

    String usrLocation ;

    String lat = "";

    String lon = "";

    Double setLatitude;

    Double setLongitude;

    Double currLatitude;

    Double currLongitude;

    List<AddressData> mAddData;

    ArrayList<AddressData> addList;

    //ArrayAdapter<String> adapter;

    private static final int MY_PERMISSION_REQUEST_CODE = 7171;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 7172;

    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    private GoogleApiClient mGoogleApiClient;

    private Location mLastLocation;

    private static int UPDATE_INTERVAL = 5000; // SEC

    private static int FATEST_INTERVAL = 3000; // SEC

    private static int DISPLACEMENT = 10; // METERS

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_f);

        buildGoogleApiClient();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED

                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION

            }, MY_PERMISSION_REQUEST_CODE);

        } else {

            if (checkPlayServices()) {

                buildGoogleApiClient();

                createLocationRequest();
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        userMessage = (TextView)findViewById(R.id.userMessage);

        itemCheck =  (TextView)findViewById(R.id.itemCheck);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        userMessage.setText("Have a great day!");

        itemCheck.setText("Make sure you have all of you necessary items.");

        listView = (ListView)findViewById(R.id.listItem);

        arrayList = new ArrayList<>();

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);

        DataSource.get(HomeF.this).getData(new DataSource.DataListener(){

            @Override
            public void onDataReceived(List<Data>data){

                mData = data;

                listView.setAdapter(new DataAdapter(HomeF.this, R.layout.list_view_data,data));
            }
        });

        dataName = (TextView)findViewById(R.id.dataName);

        addList = new ArrayList<>();

        //

        AddressSource.get(HomeF.this).getAddress(new AddressSource.AddressListener() {
            @Override
            public void onAddressReceived(List<AddressData> items) {

                mAddData = items;

                //spinner.setAdapter(new AddressAdapter(HomeF.this,R.layout.support_simple_spinner_dropdown_item,items));

            }
        });

        /*adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                AddressData addressData = (AddressData)parent.getItemAtPosition(position);

                usrLocation = addressData.getAddressName();
                onLocChange(usrLocation);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        }); */

        Button startBtn = (Button)findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLocChange(usrLocation);
            }
        });

       /* new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    onLocChange(usrLocation);
                }
            }
        }).start();*/


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

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);

        } else {

            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            Intent viewList = new Intent(HomeF.this,ViewList.class);

            startActivity(viewList);

        }else if (id == R.id.nav_address) {

            Intent address = new Intent(HomeF.this,AddressActivity.class);

            startActivity(address);

        }else if (id == R.id.nav_share) {

            Intent alarm = new Intent(HomeF.this,LocationSettings.class);

            startActivity(alarm);

        } else if (id == R.id.nav_send) {

            Intent accSettings = new Intent(HomeF.this,AccSettingsActivity.class);

            startActivity(accSettings);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    protected void onResume() {

        super.onResume();

        checkPlayServices();
    }

    @Override
    protected void onStart() {

        super.onStart();

        if(mGoogleApiClient != null) {

            mGoogleApiClient.connect();
        }

    }

    @Override
    protected void onStop() {

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);

        stopLocationUpdates();

        if(mGoogleApiClient != null) {mGoogleApiClient.disconnect();}

        Intent intent = new Intent(this,NotificationService.class);
        startService(intent);

        super.onStop();
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
        Log.d("Carma", "Client made");

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

        if(mRequestingLocationUpdates)

            startLocationUpdates();
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

            currLatitude = mLastLocation.getLatitude();

            currLongitude = mLastLocation.getLongitude();
        }

    }

    public void onLocChange(String location) {

        lat = "";

        lon = "";

        if(location == "" || location == null){

            Log.d("Deletion", "We out dis bih");

            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED

                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.d("Cremae", "We in dis bih");
        if (mLastLocation != null) {

            Log.d("Cremaee", "We really in dis bih");

            currLatitude = mLastLocation.getLatitude();

            currLongitude = mLastLocation.getLongitude();

            /*String[] parts = location.split(",", 2);

            lat = parts[0];

            lon = parts[1];

            setLatitude = Double.parseDouble(lat);

            setLongitude = Double.parseDouble(lon);
            */

            setLatitude =  40.598300;
            setLongitude = -73.762960;

            float[] dist = new float[1];

            Log.d("myLat", setLatitude.toString());

            Log.d("myLon", setLongitude.toString());

            Log.d("CurrLat", currLatitude.toString());

            Log.d("CurrLon", currLongitude.toString());

            Location.distanceBetween(setLatitude, setLongitude, currLatitude, currLongitude, dist);

            if (dist[0] / 100 > 1) {

                final AlertDialog.Builder dialog = new AlertDialog.Builder(HomeF.this);

                dialog.setTitle("Final Check").setMessage("Do you have everything you need?").setPositiveButton("Open my list to check. Just in case.",

                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(HomeF.this, ViewList.class);

                                startActivity(intent);
                            }
                        }).setNegativeButton("Yes, I do! Thank You!", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                dialog.show();
            }
        }
        else{

            Log.d("Cremaett", "Check local not gud");

        }
    }

}