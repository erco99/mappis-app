package com.example.mappis.CardMaps;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mappis.Database.MapRepository;

import java.util.List;

public class CardViewModel extends AndroidViewModel {

    private LiveData<List<CardItem>> cardItems;
    private final MutableLiveData<CardItem> itemSelected = new MutableLiveData<>();
    private MapRepository repository;

    public CardViewModel(@NonNull Application application) {
        super(application);
        repository = new MapRepository(application);
        cardItems = repository.getCardItemList();
    }

    public void select(CardItem cardItem) {
        itemSelected.setValue(cardItem);
    }

    public LiveData<CardItem> getSelected() {
        return itemSelected;
    }

    public LiveData<List<CardItem>> getCardItems() {
        return cardItems;
    }

    public CardItem getCardItem(int position){
        return cardItems.getValue() == null ? null : cardItems.getValue().get(position);
    }

}