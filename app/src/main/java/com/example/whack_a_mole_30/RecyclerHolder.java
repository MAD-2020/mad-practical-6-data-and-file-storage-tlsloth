package com.example.whack_a_mole_30;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerHolder extends RecyclerView.ViewHolder {
    TextView level;
    TextView Score;

    public RecyclerHolder(View itemView, final RecyclerAdapter.OnItemClickListener onItemClickListener){
        super(itemView);
        level =itemView.findViewById(R.id.LevelText);
        Score = itemView.findViewById(R.id.HighestScoreText);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    int position = getAdapterPosition();
                    onItemClickListener.OnItemClick(position);
                }
            }
        });
    }
}
