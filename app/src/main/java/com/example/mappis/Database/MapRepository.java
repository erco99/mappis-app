package com.example.mappis.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mappis.CardMaps.CardItem;

import java.util.List;

public class MapRepository {

    private CardItemDAO cardItemDAO;
    private LiveData<List<CardItem>> cardItemList;
    private MapDatabase database;


    public MapRepository(Application application) {
        database = MapDatabase.getDatabase(application);
        cardItemDAO = database.cardItemDAO();
        cardItemList = cardItemDAO.getCardItems();
    }

    public LiveData<List<CardItem>> getCardItemList() {
        return cardItemList;
    }

    public void addCardItem(final CardItem cardItem) {
        MapDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                cardItemDAO.addCardItem(cardItem);
            }
        });
    }
}
