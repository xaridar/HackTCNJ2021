package com.topperbibb.hacktcnj2021.client.game.graphics;

public class SpriteInfo {
    int size, x, y;
    Spritesheet sheet;

    public SpriteInfo(Spritesheet sheet, int size, int x, int y) {
        this.sheet = sheet;
        this.size = size;
        this.x = x;
        this.y = y;
    }
}
