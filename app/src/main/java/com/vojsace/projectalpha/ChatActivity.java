package com.vojsace.projectalpha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private Button btnSave;
    private EditText msg_input;
    private RecyclerView recyclerView;

    DatabaseReference ref;
    private String room_name, current_user;
    private String temp_key;

    private FirebaseRecyclerOptions<model> options;
    private FirebaseRecyclerAdapter<model, myViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        current_user = getIntent().getExtras().get("user_name").toString();
        room_name = getIntent().getExtras().get("room_name").toString();
        setTitle("Room - " + room_name);

        btnSave = (Button)findViewById(R.id.chat_btn);
        msg_input = (EditText)findViewById(R.id.chat_editText);
        ref = FirebaseDatabase.getInstance().getReference("rooms/" + room_name);
        recyclerView = findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<String, Object>();
                temp_key = ref.push().getKey();
                ref.updateChildren(map);

                DatabaseReference message_root = ref.child(temp_key);
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("name", current_user);
                map2.put("msg", msg_input.getText().toString());

                message_root.updateChildren(map2);
                msg_input.setText("");
            }
        });

        options = new FirebaseRecyclerOptions.Builder<model>().setQuery(ref, model.class).build();
        adapter = new FirebaseRecyclerAdapter<model, myViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i, @NonNull model model) {
                myViewHolder.textViewUser.setText(model.getName());
                myViewHolder.textViewMsg.setText(model.getMsg());
            }

            @NonNull
            @Override
            public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
              View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.single_message_layout, parent, false);

                return new myViewHolder(v);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}
