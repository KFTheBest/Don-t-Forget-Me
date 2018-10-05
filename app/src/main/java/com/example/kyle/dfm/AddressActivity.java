package com.example.kyle.dfm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
    }
    public void saveButtonClicked(View v){
        String messageText = ((EditText)findViewById(R.id.message)).getText().toString();

        if(messageText.equals("") || messageText.length() == 0 || messageText==null){
            Toast.makeText(AddressActivity.this, "Please enter an item to be saved!", Toast.LENGTH_LONG).show();
        }
        else{
            Intent intent = new Intent();
            intent.putExtra(Intent_Constants.INTENT_MESSAGE_FIELD,messageText);
            setResult(Intent_Constants.INTENT_RESULT_CODE,intent);
            Data data = new Data(messageText);
            DataSource.get(AddressActivity.this).sendData(data);

            finish();
            super.onBackPressed();
        }
    }

}
