package com.topperbibb.hacktcnj2021.client.game.tiles;

import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;

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

    SpriteInfo sprite;
    TileDescriptor descriptor;
    ArrayList<TileTags> tags = new ArrayList<>();

    public TileInfo(SpriteInfo sprite, TileDescriptor descriptor, ArrayList<TileTags> tags) {
        this.sprite = sprite;
        this.descriptor = descriptor;
        this.tags = tags;
    }

    public TileInfo(SpriteInfo sprite, TileDescriptor descriptor) {
        this.sprite = sprite;
        this.descriptor = descriptor;
    }

    public SpriteInfo getSprite() {
        return sprite;
    }

    public void setSprite(SpriteInfo sprite) {
        this.sprite = sprite;
    }

    public boolean isSpawnPoint() {
        return descriptor == TileDescriptor.SPAWN_POINT;
    }

    public void setSpawnPoint() {
        this.descriptor = TileDescriptor.SPAWN_POINT;
    }

    public void removeSpawnPoint() {
        this.descriptor = TileDescriptor.NONE;
        sprite = new SpriteInfo(16, 0, 0);
    }

    public void setEndPoint() {
        this.descriptor = TileDescriptor.END_POINT;
    }

    public void addTag(TileTags tag) {
        tags.add(tag);
    }
}
