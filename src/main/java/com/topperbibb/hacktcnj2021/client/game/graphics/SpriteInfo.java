package com.topperbibb.hacktcnj2021.client.game.graphics;

public class SpriteInfo {
    public int width, height, x, y;
    FlipEnum flipType = FlipEnum.NONE;
    public String key;
    public Spritesheet spritesheet;
    public double pixelScale = SpriteManager.pixelScale;

    public SpriteInfo(int width, int height, int x, int y, String key, Spritesheet spritesheet) {
        this.spritesheet = spritesheet;
        this.key = key;
        this.width = width;;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public SpriteInfo(int width, int height, int x, int y, FlipEnum flipType, String key, Spritesheet spritesheet) {
        this.spritesheet = spritesheet;
        this.key = key;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.flipType = flipType;
    }

    public SpriteInfo(int width, int height, int x, int y, String key, Spritesheet spritesheet, double pixelScale) {
        this.pixelScale = pixelScale;
        this.spritesheet = spritesheet;
        this.key = key;
        this.width = width;;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public SpriteInfo(int width, int height, int x, int y, FlipEnum flipType, String key, Spritesheet spritesheet, double pixelScale) {
        this.pixelScale = pixelScale;
        this.spritesheet = spritesheet;
        this.key = key;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.flipType = flipType;
    }

    public SpriteInfo flipped(FlipEnum flipType) {
        return new SpriteInfo(width, height, x, y, this.flipType.flipped(flipType), key + "_flipped_" + flipType.name().toLowerCase(), this.spritesheet);
    }

    public boolean flipX() {
        return flipType.flipX();
    }

    public boolean flipY() {
        return flipType.flipY();
    }

}
