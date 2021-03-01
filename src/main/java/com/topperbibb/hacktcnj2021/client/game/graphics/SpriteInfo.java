package com.topperbibb.hacktcnj2021.client.game.graphics;

public class SpriteInfo {
    public int size, x, y;
    FlipEnum flipType = FlipEnum.NONE;
    String key;

    public SpriteInfo(int size, int x, int y, String key) {
        this.key = key;
        this.size = size;
        this.x = x;
        this.y = y;
    }

    public SpriteInfo(int size, int x, int y, FlipEnum flipType, String key) {
        this.key = key;
        this.size = size;
        this.x = x;
        this.y = y;
        this.flipType = flipType;
    }

    public SpriteInfo flipped(FlipEnum flipType) {
        return new SpriteInfo(size, x, y, this.flipType.flipped(flipType), key + "_flipped_" + flipType.name().toLowerCase());
    }

    public boolean flipX() {
        return flipType.flipX();
    }

    public boolean flipY() {
        return flipType.flipY();
    }

}
