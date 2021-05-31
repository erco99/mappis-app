package com.example.mappis.CardMaps;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mappis.R;

public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView nameTextView;
    TextView descriptionTextView;
    ImageView mapImageView;

    private final OnItemListener itemListener;

    public CardViewHolder(@NonNull View itemView, OnItemListener listener) {
        super(itemView);
        this.nameTextView = itemView.findViewById(R.id.nameTextView);
        this.descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        this.mapImageView = itemView.findViewById(R.id.mapImageView);
        this.itemListener = listener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.itemListener.onItemClick(getAdapterPosition());
    }
}
