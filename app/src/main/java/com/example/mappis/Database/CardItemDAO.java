package com.example.mappis.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.mappis.CardMaps.CardItem;
import com.example.mappis.CardMaps.Comments.Comment;

import java.util.List;

@Dao
public interface CardItemDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addCardItem(CardItem cardItem);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addComment(Comment comment);

    @Transaction
    @Query("SELECT * FROM item ORDER BY item_id DESC")
    LiveData<List<CardItem>> getCardItems();

    @Transaction
    @Query("SELECT * FROM comment WHERE card_item_id = :cardItemId")
    LiveData<List<Comment>> getComments(int cardItemId);
}
