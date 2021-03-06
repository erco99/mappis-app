package com.example.mappis.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
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

    @Query("SELECT * FROM comment WHERE card_item_id = :id ORDER BY comment_id DESC")
    LiveData<List<Comment>> getComments(int id);

    @Query("SELECT MAX(item_id) FROM item")
    LiveData<Integer> getLastId();

    @Query("DELETE FROM item WHERE item_id = :itemId")
    void deleteCardItem(int itemId);

    @Query("DELETE FROM comment WHERE card_item_id = :cardItemId")
    void deleteComments(int cardItemId);

    @Query("UPDATE item SET item_image_uri = :uri WHERE item_id = :id")
    void changeImage(String uri, int id);
}
