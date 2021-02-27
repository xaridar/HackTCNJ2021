package com.topperbibb.hacktcnj2021.client.game;

import java.awt.image.BufferedImage;

public class TileInfo {

    BufferedImage sprite;
    boolean spawnPoint;

    public BufferedImage getSprite() {
        return sprite;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public boolean isSpawnPoint() {
        return spawnPoint;
    }

    public void setSpawnPoint(boolean spawnPoint) {
        this.spawnPoint = spawnPoint;
    }
}
