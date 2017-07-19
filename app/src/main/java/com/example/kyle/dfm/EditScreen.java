package com.example.kyle.dfm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditScreen extends ViewList {
    String MessageText;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);
        Intent intent = getIntent();
        MessageText = intent.getStringExtra(Intent_Constants.INTENT_MESSAGE_DATA);
        position = intent.getIntExtra(Intent_Constants.INTENT_ITEM_POSTION, -1);
        EditText messageDATA = (EditText)findViewById(R.id.message);
        messageDATA.setText(MessageText);
    }
    public void saveButtonClicked(View v){
        String changedMessageText = ((EditText)findViewById(R.id.message)).getText().toString();
        Intent intent = new Intent();
        intent.putExtra(Intent_Constants.INTENT_CHANGED_MESSAGE, changedMessageText);
        intent.putExtra(Intent_Constants.INTENT_ITEM_POSTION,position);
        setResult(Intent_Constants.INTENT_RESULT_CODE_TWO,intent);
        editor.putString("waa",changedMessageText);
        editor.apply();
        editor.commit();

        finish();
    }
    }

