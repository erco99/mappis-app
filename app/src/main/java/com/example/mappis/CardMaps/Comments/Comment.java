package com.example.mappis.CardMaps.Comments;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "comment")
public class Comment {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "comment_id")
    private int commentId;

    @ColumnInfo(name = "card_item_id")
    private int cardItemId;


    @ColumnInfo(name = "comment")
    private String comment;

    public Comment(String comment, int cardItemId) {
        this.comment = comment;
        this.cardItemId = cardItemId;

    }

    public String getComment() {
        return comment;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getCardItemId() {
        return cardItemId;
    }

}
