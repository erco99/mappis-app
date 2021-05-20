package com.example.mappis.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mappis.CardMaps.CardItem;
import com.example.mappis.CardMaps.Comments.Comment;

import java.util.List;

public class MapRepository {

    private final CardItemDAO cardItemDAO;
    private final LiveData<List<CardItem>> cardItemList;

    public MapRepository(Application application) {
        MapDatabase database = MapDatabase.getDatabase(application);
        cardItemDAO = database.cardItemDAO();
        cardItemList = cardItemDAO.getCardItems();
    }

    public LiveData<List<CardItem>> getCardItemList() {
        return cardItemList;
    }

    public LiveData<List<Comment>> getCommentList(int id) {

        return cardItemDAO.getComments(id);
    }

    public void addCardItem(final CardItem cardItem) {
        MapDatabase.databaseWriteExecutor.execute(() -> cardItemDAO.addCardItem(cardItem));
    }

    public void addComment(final Comment comment) {
        MapDatabase.databaseWriteExecutor.execute(() -> cardItemDAO.addComment(comment));
    }
}
