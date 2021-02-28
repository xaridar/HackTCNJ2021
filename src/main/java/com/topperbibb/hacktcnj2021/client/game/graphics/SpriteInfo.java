package com.topperbibb.hacktcnj2021.client.game.graphics;

public class SpriteInfo {
    int size, x, y;
    boolean reversed = false;

    public SpriteInfo(int size, int x, int y) {
        this.size = size;
        this.x = x;
        this.y = y;
    }

    public SpriteInfo(int size, int x, int y, boolean reversed) {
        this.size = size;
        this.x = x;
        this.y = y;
        this.reversed = reversed;
    }

    public SpriteInfo reversed() {
        return new SpriteInfo(size, x, y, !reversed);
    }
}
