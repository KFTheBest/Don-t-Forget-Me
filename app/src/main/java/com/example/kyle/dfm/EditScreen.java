package com.example.kyle.dfm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditScreen extends AppCompatActivity {
    String MessageText;
    int position;
    Data mData;
    EditText editText;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_screen);

        Intent intent = getIntent();
        mData = (Data)intent.getSerializableExtra("DataObject");


       MessageText = intent.getStringExtra(Intent_Constants.INTENT_MESSAGE_DATA);
        position = intent.getIntExtra(Intent_Constants.INTENT_ITEM_POSTION, -1);
        EditText messageDATA = (EditText)findViewById(R.id.message);
        messageDATA.setText(MessageText);

       editText = (EditText)findViewById(R.id.editBar);
        editText.setText(mData.getDataName());
        saveButton = (Button)findViewById(R.id.saveButton);

    }
    public void saveButtonClicked(View v){
        String changedMessageText = ((EditText)findViewById(R.id.message)).getText().toString();

        if(changedMessageText.equals("") || changedMessageText.length() == 0 || changedMessageText==null){
            Toast.makeText(EditScreen.this, "Please enter an item to be saved!", Toast.LENGTH_LONG).show();
        }
        else{
            Intent intent = new Intent();
            intent.putExtra(Intent_Constants.INTENT_CHANGED_MESSAGE, changedMessageText);
            intent.putExtra(Intent_Constants.INTENT_ITEM_POSTION,position);
            setResult(Intent_Constants.INTENT_RESULT_CODE_TWO,intent);

            Data data = new Data(changedMessageText);
            DataSource.get(EditScreen.this).sendData(data);


            finish();
        }



    }
    }

