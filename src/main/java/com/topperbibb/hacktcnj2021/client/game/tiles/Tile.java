package com.topperbibb.hacktcnj2021.client.game.tiles;

import com.topperbibb.hacktcnj2021.client.game.objects.BoardObject;

public class Tile {
    BoardObject object;
    TileInfo info;
    int x;
    int y;

    public Tile(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Tile(int x, int y, TileInfo info){
        this.x = x;
        this.y = y;
        this.info = info;
    }

    public Tile(int x, int y, TileInfo info, BoardObject object){
        this.x = x;
        this.y = y;
        this.info = info;
        this.object = object;
    }

    public Tile(TileInfo info, BoardObject object){
        this.x = 0;
        this.y = 0;
        this.info = info;
        this.object = object;
    }

    public Tile(TileInfo info){
        this.x = 0;
        this.y = 0;
        this.info = info;
    }

    public TileInfo getInfo() {
        return info;
    }

    public void setInfo(TileInfo info) {
        this.info = info;
    }

    public BoardObject getObject() {
        return object;
    }

    public void setObject(BoardObject object) {
        this.object = object;
    }

    public String getSprite() {
        if (object != null) {
            return object.getSprite();
        } else {
            return info.getSprite();
        }
    }

    public void addTag(TileTags tag) {
        info.addTag(tag);
    }

    public boolean hasTag(TileTags tag) {
        return info.tags.contains(tag);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
