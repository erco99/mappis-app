package com.example.mappis.CardMaps;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mappis.R;

public class CardViewHolder extends RecyclerView.ViewHolder {

    TextView nameTextView;
    TextView descriptionTextView;

    public CardViewHolder(@NonNull View itemView) {
        super(itemView);
        this.nameTextView = itemView.findViewById(R.id.nameTextView);
        this.descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
    }
}
