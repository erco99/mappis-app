package com.example.mappis.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mappis.CardMaps.CardItem;
import com.example.mappis.CardMaps.Comments.Comment;

import java.util.List;

public class MapRepository {

    private CardItemDAO cardItemDAO;
    private LiveData<List<CardItem>> cardItemList;


    private LiveData<List<Comment>> commentList;
    private MapDatabase database;

    private int id;


    public MapRepository(Application application) {
        database = MapDatabase.getDatabase(application);
        cardItemDAO = database.cardItemDAO();
        cardItemList = cardItemDAO.getCardItems();
    }

    public void setId(int id) {
        this.id = id;
        setCommentList();
    }
    private void setCommentList() {
        this.commentList = cardItemDAO.getComments(id);
    }

    public LiveData<List<CardItem>> getCardItemList() {
        return cardItemList;
    }

    public LiveData<List<Comment>> getCommentList() {
        return commentList;
    }

    public void addCardItem(final CardItem cardItem) {
        MapDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                cardItemDAO.addCardItem(cardItem);
            }
        });
    }

    public void addComment(final Comment comment) {
        MapDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                cardItemDAO.addComment(comment);
            }
        });
    }
}
