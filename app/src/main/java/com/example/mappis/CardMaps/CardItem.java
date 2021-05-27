package com.example.mappis.CardMaps;

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

    public CardItem(int itemId, String mapName, String mapDescription, String mapType) {
        this.itemId = itemId;
        this.mapName = mapName;
        this.mapDescription = mapDescription;
        this.mapType = mapType;
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
