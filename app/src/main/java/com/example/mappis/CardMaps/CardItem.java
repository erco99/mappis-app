package com.example.mappis.CardMaps;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item")
public class CardItem {

    @PrimaryKey
    @ColumnInfo(name = "item_id")
    private final int itemId;

    @ColumnInfo(name = "item_map_name")
    private final String mapName;

    @ColumnInfo(name = "item_map_description")
    private final String mapDescription;

    @ColumnInfo(name = "item_map_type")
    private final String mapType;

    @ColumnInfo(name = "item_image_uri")
    private String mapImageUri;

    public CardItem(int itemId, String mapName, String mapDescription, String mapType, String mapImageUri) {
        this.itemId = itemId;
        this.mapName = mapName;
        this.mapDescription = mapDescription;
        this.mapType = mapType;
        this.mapImageUri = mapImageUri;
    }

    public String getMapImageUri() {
        return mapImageUri;
    }

    public String getMapType() {
        return mapType;
    }

    public String getMapName() {
        return mapName;
    }

    public String getMapDescription() {
        return mapDescription;
    }

    public int getItemId() {
        return itemId;
    }
}
