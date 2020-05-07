package com.vojsace.projectalpha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class GroupActivity extends AppCompatActivity {
    private Button add_room;
    private EditText room_name;

    private ListView listView;
    private ArrayAdapter<String> array_adapter;
    private ArrayList<String> list_of_rooms = new ArrayList<>();

    private String name;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("rooms");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        add_room = (Button)findViewById(R.id.btn_add_room);
        room_name = (EditText) findViewById(R.id.room_name_edittext);
        listView = (ListView) findViewById(R.id.listView);

        array_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_of_rooms);

        listView.setAdapter(array_adapter);

        request_user_name();

        add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(room_name.getText().toString(),"");
                root.updateChildren(map);
            }
        });
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()){
                    set.add(((DataSnapshot) i.next()).getKey());
                }
                list_of_rooms.clear();
                list_of_rooms.addAll(set);

                array_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("room_name", ((TextView)view).getText().toString());
                intent.putExtra("user_name", name);
                startActivity(intent);
            }
        });
    }

    private void request_user_name(){
        name = getIntent().getExtras().getString("user_name");

    }

}
