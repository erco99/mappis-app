package com.example.mappis.CardMaps;

public class CardItem {

    private String mapName;
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
}
