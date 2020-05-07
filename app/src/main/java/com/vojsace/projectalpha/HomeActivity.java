package com.vojsace.projectalpha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "ViewDatabase";

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference reference;
    private String userID;

    private TextView greeting;
    private String name;
    private Button goToGroup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        greeting = (TextView)findViewById(R.id.greeting);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        FirebaseUser user = auth.getCurrentUser();
        userID = user.getUid();

        goToGroup = (Button) findViewById(R.id.goToGroup);

        request_user_name();


    }
//Don't touch this For getting User information reguest_user_name
    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authListener != null){
            auth.removeAuthStateListener(authListener);
        }
    }

    private void request_user_name() {
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null){
                    Log.d(TAG, "onAuthStateChanged: signed_in" + user.getUid());
                }else{
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }

            }
        };

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()){
            final UserInfo userInfo = new UserInfo();
            userInfo.setUsername(ds.child(userID).getValue(UserInfo.class).getUsername());

            Log.d(TAG, "showData username: " + userInfo.getUsername());

            greeting.setText("Hello, " + userInfo.getUsername());

            goToGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(HomeActivity.this, GroupActivity.class);
                    i.putExtra("user_name", userInfo.getUsername());
                    startActivity(i);
                }
            });

        }

    }
    //*************************TILL HERE****************************
}
