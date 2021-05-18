package com.example.mappis.CardMaps;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mappis.CardMaps.Comments.Comment;
import com.example.mappis.Database.MapRepository;

import java.util.List;

public class CardViewModel extends AndroidViewModel {

    private LiveData<List<CardItem>> cardItems;
    private LiveData<List<Comment>> comments;
    private final MutableLiveData<CardItem> itemSelected = new MutableLiveData<>();
    private MapRepository repository;

    private int id;

    public CardViewModel(@NonNull Application application) {
        super(application);
        repository = new MapRepository(application);
        cardItems = repository.getCardItemList();

    }

    public void setId(int id) {
        repository.setId(id);
        setComments();
    }

    private void setComments() {
        this.comments = repository.getCommentList();
    }

    public void select(CardItem cardItem) {
        itemSelected.setValue(cardItem);
    }

    public LiveData<CardItem> getSelected() {
        return itemSelected;
    }

    public LiveData<List<Comment>> getComments() {
        return comments;
    }

    public LiveData<List<CardItem>> getCardItems() {

        return cardItems;
    }

    public CardItem getCardItem(int position){
        return cardItems.getValue() == null ? null : cardItems.getValue().get(position);
    }

}
