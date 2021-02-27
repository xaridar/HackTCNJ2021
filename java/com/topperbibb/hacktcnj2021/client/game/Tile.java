package com.topperbibb.hacktcnj2021.client.game;

public class Tile {
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
}
