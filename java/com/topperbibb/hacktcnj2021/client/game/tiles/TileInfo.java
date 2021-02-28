package com.topperbibb.hacktcnj2021.client.game.tiles;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class TileInfo {

    public enum TileDescriptor {
        SPAWN_POINT(0), END_POINT(1), NONE(2);

        int i;
        TileDescriptor(int i) {
            this.i = i;
        }
    }

    String sprite;
    TileDescriptor descriptor;
    ArrayList<TileTags> tags;

    public TileInfo(String sprite, TileDescriptor descriptor) {
        this.sprite = sprite;
        this.descriptor = descriptor;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    public boolean isSpawnPoint() {
        return descriptor == TileDescriptor.SPAWN_POINT;
    }

    public void setSpawnPoint() {
        this.descriptor = TileDescriptor.SPAWN_POINT;
        sprite = "o";
    }

    public void setEndPoint() {
        this.descriptor = TileDescriptor.END_POINT;
        sprite = "d";
    }

    public void addTag(TileTags tag) {
        tags.add(tag);
    }
}
