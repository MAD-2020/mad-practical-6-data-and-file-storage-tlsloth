package com.example.whack_a_mole_30;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerHolder> {
    private ArrayList<Integer> data;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
    this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleritem,parent,false);
        return new RecyclerHolder(item,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        String level = "Level "+(position+1);
        String score = "Highest Score: "+data.get(position);
        holder.level.setText(level);
        holder.Score.setText(score);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }
    public RecyclerAdapter(ArrayList<Integer> data){
        this.data = data;
    }


}
