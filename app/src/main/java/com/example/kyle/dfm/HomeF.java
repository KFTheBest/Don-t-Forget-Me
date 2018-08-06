package com.example.kyle.dfm;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.dynamic.SupportFragmentWrapper;

import java.util.ArrayList;
import java.util.List;

public class HomeF extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private android.widget.TextView dayTxt; //REMEMBER TO CHANGE
    private android.widget.TextView uhhTxt; //REMEMBER TO CHANGE



    ListView listView;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    List<Data> mData;
    TextView dataName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_f);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        dayTxt = (TextView)findViewById(R.id.day);
        uhhTxt =  (TextView)findViewById(R.id.uhhTxt);


        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                CreateList listFrag = new CreateList();
                fragmentTransaction.add(R.id.addlist1,listFrag);
                fragmentTransaction.commit(); */

                Intent viewList = new Intent(HomeF.this,CreateList.class);

                startActivity(viewList);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);





       dayTxt.setText("Have a great day!");
       uhhTxt.setText("Make sure you have all of you necessary items.");





        listView = (ListView)findViewById(R.id.listItem);


        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        //listView.setAdapter(arrayAdapter);
        DataSource.get(HomeF.this).getData(new DataSource.DataListener(){
            @Override
            public void onDataReceived(List<Data>data){
                mData = data;
                listView.setAdapter(new DataAdapter(HomeF.this, R.layout.list_view_data,data));
            }
        });


        dataName = (TextView)findViewById(R.id.dataName);





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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action

            Intent viewList = new Intent(HomeF.this,ViewList.class);

            startActivity(viewList);

        } else if (id == R.id.nav_share) {
            Intent alarm = new Intent(HomeF.this,Alarm.class);

            startActivity(alarm);


        } else if (id == R.id.nav_send) {

            Intent accSettings = new Intent(HomeF.this,AccSettingsActivity
                    .class);

            startActivity(accSettings);



        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
