package com.example.mappis.CardMaps;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mappis.R;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardViewHolder> {

    private List<CardItem> cardItemList;

    private Activity activity;

    public CardAdapter(Activity activity, List<CardItem> cardItemList) {
        this.activity = activity;
        this.cardItemList = cardItemList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.maps_card_layout,
                parent, false);
        return new CardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardItem currentCardItem = cardItemList.get(position);

        holder.nameTextView.setText(currentCardItem.getMapName());
        holder.descriptionTextView.setText(currentCardItem.getMapDescription());
    }

    @Override
    public int getItemCount() {
        return cardItemList.size();
    }
}
