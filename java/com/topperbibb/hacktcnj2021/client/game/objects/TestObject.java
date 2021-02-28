package com.topperbibb.hacktcnj2021.client.game.objects;

public class TestObject implements RigidBoardObject{

    String sprite;

    public TestObject(String sprite) {
        setSprite(sprite);
    }

    @Override
    public String getSprite() {
        return sprite;
    }

    @Override
    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    @Override
    public boolean move(int directionX, int directionY) {
        return false;
    }
}
