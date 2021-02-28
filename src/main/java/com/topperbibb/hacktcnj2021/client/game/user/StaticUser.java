package com.topperbibb.hacktcnj2021.client.game.user;

import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;
import com.topperbibb.hacktcnj2021.client.game.objects.StaticBoardObject;

import java.util.Map;

public class StaticUser extends NetUser implements StaticBoardObject {

    enum StaticDirection {
        UP, DOWN, LEFT, RIGHT,
    }

    public int x;
    public int y;

    private StaticDirection dir;
    private Map<StaticDirection, SpriteInfo> sprites;

    @Override
    public SpriteInfo getSprite() {
        return sprites.get(dir);
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
}
