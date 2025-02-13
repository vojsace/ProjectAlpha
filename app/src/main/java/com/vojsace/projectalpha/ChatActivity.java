package com.vojsace.projectalpha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import Model.model;

public class ChatActivity extends AppCompatActivity {
    private ImageButton btnSave;
    private EditText msg_input;
    private RecyclerView recyclerView;
    private TextView questionText;

    DatabaseReference ref;
    private String room_name, current_user, color, question_color;
    private String temp_key;
    private boolean likeClicked = false;
    private int likes_count = 0;

    private FirebaseRecyclerOptions<model> options;
    private FirebaseRecyclerAdapter<model, myViewHolder> adapter;

    private DatabaseReference mDatabaseLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        current_user = getIntent().getExtras().getString("user_name");
        room_name = getIntent().getExtras().getString("room_name");
        color = getIntent().getExtras().getString("user_color");
        question_color = getIntent().getExtras().getString("question_color");
        questionText = (TextView) findViewById(R.id.scrollText);
        questionText.setBackgroundColor(Integer.parseInt(question_color));
        questionText.setText(room_name); //adds the question

        btnSave = (ImageButton)findViewById(R.id.imageButton);
        msg_input = (EditText)findViewById(R.id.chat_editText);
        ref = FirebaseDatabase.getInstance().getReference("rooms/" + room_name);
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
        mDatabaseLike.keepSynced(true);
        ref.keepSynced(true);
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
            protected void onBindViewHolder(@NonNull final myViewHolder myViewHolder, int i, @NonNull final model model) {
                String msg_id = model.getMsg_id();
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

                    mDatabaseLike.child(room_name).child(msg_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            likes_count = (int) dataSnapshot.getChildrenCount();
                            myViewHolder.likeText.setText(String.valueOf(likes_count));

                            if (dataSnapshot.hasChild(current_user)){
                                myViewHolder.likeBtn.setBackgroundResource(R.drawable.like);
                            }else
                                myViewHolder.likeBtn.setBackgroundResource(R.drawable.dislike);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    myViewHolder.likeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            likeClicked = true;

                                mDatabaseLike.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String msg_id = model.getMsg_id();
                                     if (likeClicked) {
                                         if (dataSnapshot.child(room_name).child(msg_id).hasChild(current_user)){
                                             Log.d("unlike_msg", "success");
                                             mDatabaseLike.child(room_name).child(msg_id).child(current_user).removeValue();
                                         }else{
                                             mDatabaseLike.child(room_name).child(msg_id).child(current_user).setValue("true");
                                             Log.d("unlike_msg", "success - liked");
                                         }
                                         likeClicked = false;
                                     }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }


                                });



                        }
                    });

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
            Log.d("msg_id", temp_key);

            DatabaseReference message_root = ref.child(temp_key);
            Map<String, Object> map2 = new HashMap<String, Object>();
            map2.put("name", current_user);
            map2.put("msg", msg_input.getText().toString());
            map2.put("user_color", color);
            map2.put("msg_id", temp_key);
            message_root.updateChildren(map2);
            msg_input.setText("");
            recyclerView.scrollToPosition(adapter.getItemCount()); //scroll to the last message
        }
    }
}
