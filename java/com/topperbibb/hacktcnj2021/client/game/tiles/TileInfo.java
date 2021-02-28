package com.topperbibb.hacktcnj2021.client.game.tiles;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class TileInfo {

    String sprite;
    boolean spawnPoint;
    boolean endPoint;
    ArrayList<TileTags> tags;

    public TileInfo(String sprite, boolean spawnPoint) {
        this.sprite = sprite;
        this.spawnPoint = spawnPoint;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    public boolean isSpawnPoint() {
        return spawnPoint;
    }

    public void setSpawnPoint(boolean spawnPoint) {
        this.spawnPoint = spawnPoint;
        sprite = "o";
    }

    public void setEndPoint(boolean endPoint) {
        this.endPoint = endPoint;
        sprite = "d";
    }

    public void addTag(TileTags tag) {
        tags.add(tag);
    }
}
