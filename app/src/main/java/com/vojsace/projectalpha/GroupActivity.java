package com.vojsace.projectalpha;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;


public class GroupActivity extends AppCompatActivity {

    private TextView room_name;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        room_name = (TextView) findViewById(R.id.group_text);

        request_user_name();
        room_name.setText(name);

    }

    private void request_user_name(){
        name = getIntent().getExtras().getString("user_name");

    }

}
