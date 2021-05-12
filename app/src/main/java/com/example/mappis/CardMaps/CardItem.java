package com.example.mappis.CardMaps;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item")
public class CardItem {

    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = "item_id")
    private int id;

    @ColumnInfo(name = "item_map_name")
    private String mapName;

    @ColumnInfo(name = "item_map_description")
    private String mapDescription;

    public CardItem(String mapName, String mapDescription) {
        this.mapName = mapName;
        this.mapDescription = mapDescription;
    }

    public String getMapName() {
        return mapName;
    }

    public String getMapDescription() {
        return mapDescription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
