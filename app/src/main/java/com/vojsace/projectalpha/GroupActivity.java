package com.vojsace.projectalpha;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class GroupActivity extends AppCompatActivity {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        textView = (TextView) findViewById(R.id.group_text);


        String name = getIntent().getExtras().getString("user_name");
        textView.setText(name);
    }
}
