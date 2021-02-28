package com.topperbibb.hacktcnj2021.client.game.objects;

import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;

/**
 * An interface for non-static objects
 */
public interface BoardObject {
    public SpriteInfo getSprite();

    public int getX();

    public int getY();

    public void setPos(int x, int y);
}
