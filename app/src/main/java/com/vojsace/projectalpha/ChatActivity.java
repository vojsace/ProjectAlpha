package com.vojsace.projectalpha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.common.ChangeEventType;
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
    private ImageButton btnSave;
    private EditText msg_input;
    private RecyclerView recyclerView;
    private TextView scrollText;

    DatabaseReference ref;
    private String room_name, current_user, color;
    private String temp_key;

    private FirebaseRecyclerOptions<model> options;
    private FirebaseRecyclerAdapter<model, myViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        current_user = getIntent().getExtras().get("user_name").toString();
        room_name = getIntent().getExtras().get("room_name").toString();
        color = getIntent().getExtras().get("user_color").toString();
        scrollText = (TextView) findViewById(R.id.scrollText);
        scrollText.setText(room_name); //adds the question

        btnSave = (ImageButton)findViewById(R.id.imageButton);
        msg_input = (EditText)findViewById(R.id.chat_editText);
        ref = FirebaseDatabase.getInstance().getReference("rooms/" + room_name);
        recyclerView = findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        msg_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendMsg();
                    handled = true;
                }
                return handled;
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg();
            }
        });

        options = new FirebaseRecyclerOptions.Builder<model>().setQuery(ref, model.class).build();
        adapter = new FirebaseRecyclerAdapter<model, myViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i, @NonNull model model) {
                Drawable img = getResources().getDrawable(R.drawable.user_msg_rounded);
                if (current_user.equals(model.getName())) {
                    myViewHolder.usrLayout.setGravity(Gravity.END);
                    img.setTint(Integer.parseInt(color));
                    myViewHolder.textViewMsg.setBackground(img);

                }else {
                    myViewHolder.usrLayout.setGravity(Gravity.START);
                    img.setTint(Integer.parseInt(model.getUser_color()));
                    myViewHolder.textViewMsg.setBackground(img);
                }
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

    public void sendMsg (){
        if (!msg_input.getText().toString().equals("")){
            Map<String, Object> map = new HashMap<String, Object>();
            temp_key = ref.push().getKey();
            ref.updateChildren(map);

            DatabaseReference message_root = ref.child(temp_key);
            Map<String, Object> map2 = new HashMap<String, Object>();
            map2.put("name", current_user);
            map2.put("msg", msg_input.getText().toString());
            map2.put("user_color", color);
            message_root.updateChildren(map2);
            msg_input.setText("");
            recyclerView.scrollToPosition(adapter.getItemCount()); //scroll to the last message
        }
    }
}
