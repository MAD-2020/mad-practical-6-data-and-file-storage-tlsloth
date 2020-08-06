package com.example.whack_a_mole_30;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;

public class SelectLevel extends AppCompatActivity {
    ArrayList<Integer> scores;
    Button back;
    RecyclerAdapter adapter;
    RecyclerView recyclerView;
    DBHandler dbHandler = new DBHandler(this,null,null,1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_level);
        scores = dbHandler.getScores(this);
        back = findViewById(R.id.Backbutton);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new RecyclerAdapter(scores);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                //move to game page
                Intent intent = new Intent(SelectLevel.this,GameActivity.class);
                intent.putExtra("level",position);
                startActivity(intent);
            }
        });

    }
}