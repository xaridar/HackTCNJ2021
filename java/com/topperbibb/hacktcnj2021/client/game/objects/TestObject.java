package com.topperbibb.hacktcnj2021.client.game.objects;

import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;

public class TestObject implements RigidBoardObject{

    SpriteInfo sprite;
    int x, y;

    public TestObject(SpriteInfo sprite) {
        setSprite(sprite);
    }

    @Override
    public SpriteInfo getSprite() {
        return sprite;
    }

    @Override
    public void setSprite(SpriteInfo sprite) {
        this.sprite = sprite;
    }

    @Override
    public boolean move(int directionX, int directionY) {
        return false;
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
