package com.vojsace.projectalpha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import java.util.Random;
import java.util.Set;

import Model.QuestionModel;
import Model.UserInfo;
import Model.model;


public class GroupActivity extends AppCompatActivity {
    private Button add_room;
    private EditText room_name;

    private String name, color, question;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("rooms");
    private DatabaseReference questRef = FirebaseDatabase.getInstance().getReference("Questions");

    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<QuestionModel> options;
    private FirebaseRecyclerAdapter<QuestionModel, groupHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        add_room = (Button)findViewById(R.id.btn_add_room);
        room_name = (EditText) findViewById(R.id.room_name_edittext);
        recyclerView = findViewById(R.id.group_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        questRef.keepSynced(true);


        request_user_name();

        add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question = room_name.getText().toString();
                String clr = color;

                if (!room_name.getText().toString().equals("")){
                    writeNewQuestion(question, clr);
                }
            }
        });

        options = new FirebaseRecyclerOptions.Builder<QuestionModel>().setQuery(questRef, QuestionModel.class).build();
        adapter = new FirebaseRecyclerAdapter<QuestionModel, groupHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull groupHolder groupHolder, int i, @NonNull final QuestionModel questionModel) {
                groupHolder.questionText.setText(questionModel.getQuestion());
                groupHolder.questionText.setBackgroundColor(Integer.parseInt(questionModel.getQ_color()));
                groupHolder.questionText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                        intent.putExtra("room_name", questionModel.getQuestion());
                        intent.putExtra("user_name", name);
                        intent.putExtra("user_color", color);
                        intent.putExtra("question_color", questionModel.getQ_color());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public groupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.group_question_layout, parent, false);

                return new groupHolder(v);
            }


        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void request_user_name(){
        name = getIntent().getExtras().getString("user_name");
        color = getIntent().getExtras().getString("user_color");
    }

    private void writeNewQuestion(String question, String q_color){
        //in Questions generate new
        Random rand = new Random();
        int rand_int1 = rand.nextInt(1000);
        int rand_int2 = rand.nextInt(1000);
        int rand_int3 = rand.nextInt(1000);
        String random = String.valueOf(rand_int1) + String.valueOf(rand_int2) + String.valueOf(rand_int3);
        question = room_name.getText().toString();
        QuestionModel questionInfo = new QuestionModel();
        questionInfo.setQuestion(question);
        questionInfo.setQ_color(q_color);
        questRef.child(random).setValue(questionInfo);
         // in room also
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(room_name.getText().toString(),"");
        root.updateChildren(map);
        room_name.setText("");

    }

}
