package com.example.kyle.dfm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {
    public Button but1; // button to create new list
    public Button but2; // button to edit new list
    public Button but3; // button to view list
    public Button but4; // button to change alarm settings
    public void init(){
        but1 = (Button)findViewById(R.id.button); // initialize values
        but2 = (Button)findViewById(R.id.button2);
        but3 = (Button)findViewById(R.id.button3);
        but4 = (Button)findViewById(R.id.button4);
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // make button 1 open the create list activity
                Intent createList = new Intent(Home.this, CreateList.class);
            startActivity(createList);
            }
            });
            but2.setOnClickListener(new View.OnClickListener() { // make button 2 open the edit list activity
            @Override
            public void onClick(View v) {
                 Intent editScreen = new Intent(Home.this,EditScreen.class);

                        startActivity(editScreen);
                    }
                });
        but3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //make button 2 open the view list activity
                Intent viewList = new Intent(Home.this,ViewList.class);

                startActivity(viewList);
            }
        });
        but4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //make button 2 open the view list activity
                Intent viewList = new Intent(Home.this,ViewList.class);

                startActivity(viewList);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) { // starts the home activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
    }
}
