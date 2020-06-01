package com.vojsace.projectalpha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {
    private TextView greeting;
    private Button goToGroup;
    private static String name, clr;
    private Object color;


    private DatabaseReference ref;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        greeting = (TextView)findViewById(R.id.greeting);
        goToGroup = (Button) findViewById(R.id.goToGroup);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        String userId = auth.getUid();

        if(userId != null) {
            ref = database.getReference("Users/" + userId);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null){
                            try {
                                name = (String) snapshot.child("username").getValue();
                                color =  snapshot.child("user_color").getValue();
                                Log.d("mesage", name);
                                clr = String.valueOf(color);
                                Log.d("user_color", clr);
                                greeting.setText("Hello, " + name);

                                goToGroup.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(HomeActivity.this, GroupActivity.class);
                                        i.putExtra("user_name", name);
                                        i.putExtra("user_color", clr);
                                        startActivity(i);
                                    }
                                });
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            Log.e("TAG", "its null.");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else
            Toast.makeText(HomeActivity.this, "User empty", Toast.LENGTH_SHORT).show();

    }
}

