package com.topperbibb.hacktcnj2021.client.game.objects;

import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;

/**
 * An interface for non-static objects
 */
public interface BoardObject {
    SpriteInfo getSprite();

    void setSprite(SpriteInfo sprite);

    int getX();

    int getY();

    void setPos(int x, int y);

    BoardObject copy();
}
