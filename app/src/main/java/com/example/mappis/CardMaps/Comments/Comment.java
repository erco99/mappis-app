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
    private final int cardItemId;

    @ColumnInfo(name = "comment")
    private final String comment;

    @ColumnInfo(name = "timestamp")
    private final String timestamp;

    public Comment(String comment, int cardItemId, String timestamp) {
        this.comment = comment;
        this.cardItemId = cardItemId;
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
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
