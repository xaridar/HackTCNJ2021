package com.topperbibb.hacktcnj2021.client.game.objects;

import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;

public class Key implements BoardObject {

    SpriteInfo sprite;
    int x, y;
    boolean collected;

    public Key(SpriteInfo sprite) {
        this.sprite = sprite;
        this.x = 0;
        this.y = 0;
        this.collected = false;
    }

    public Key(SpriteInfo sprite, int x, int y, boolean collected) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        this.collected = collected;
    }

    @Override
    public SpriteInfo getSprite() {
        return sprite;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void collect() {
        collected = true;
        Board.getTileForObject(this, Board.board).setObject(null);
    }

    public boolean isCollected() {
        return collected;
    }

    @Override
    public BoardObject copy() {
        return new Key(sprite, x, y, collected);
    }
}
