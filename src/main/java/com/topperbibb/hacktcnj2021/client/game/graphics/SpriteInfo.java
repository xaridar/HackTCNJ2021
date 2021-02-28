package com.topperbibb.hacktcnj2021.client.game.graphics;

public class SpriteInfo {
    int size, x, y;
    FlipEnum flipType = FlipEnum.NONE;

    public SpriteInfo(int size, int x, int y) {
        this.size = size;
        this.x = x;
        this.y = y;
    }

    public SpriteInfo(int size, int x, int y, FlipEnum flipType) {
        this.size = size;
        this.x = x;
        this.y = y;
        this.flipType = flipType;
    }

    public SpriteInfo flipped(FlipEnum flipType) {
        return new SpriteInfo(size, x, y, this.flipType.flipped(flipType));
    }

    public boolean flipX() {
        return flipType.flipX();
    }

    public boolean flipY() {
        return flipType.flipY();
    }
}
