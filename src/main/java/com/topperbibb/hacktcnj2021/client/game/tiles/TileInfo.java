package com.topperbibb.hacktcnj2021.client.game.tiles;

import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TileInfo {


    public enum TileDescriptor {
        END_POINT, CAN_SPAWN, NO_SPAWN,
    }

    boolean isSpawn;
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

    public TileInfo(SpriteInfo sprite, TileDescriptor descriptor, ArrayList<TileTags> tags, boolean isSpawn) {
        this.isSpawn = isSpawn;
        this.sprite = sprite;
        this.descriptor = descriptor;
        this.tags = tags;
    }

    public SpriteInfo getSprite() {
        return sprite;
    }

    public void setSprite(SpriteInfo sprite) {
        this.sprite = sprite;
    }

    public boolean isSpawnPoint() {
        return isSpawn;
    }

    public void setSpawnPoint() {
        this.isSpawn = true;
    }

    public void removeSpawnPoint() {
        this.isSpawn = false;
    }

    public boolean isEndPoint() {
        return descriptor == TileDescriptor.END_POINT;
    }

    public void setEndPoint() {
        this.descriptor = TileDescriptor.END_POINT;
    }

    public void addTag(TileTags tag) {
        tags.add(tag);
    }

    public List<TileTags> getTags() {
        return tags;
    }

    public TileDescriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(TileDescriptor descriptor) {
        this.descriptor = descriptor;
    }
}
