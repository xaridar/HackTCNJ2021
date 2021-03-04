package com.topperbibb.hacktcnj2021.client.game.graphics;

/**
 * A class to contain info about a sprite for rendering, including x and y offsets, height and width, a key, and any offsets
 */
public class SpriteInfo {
    public int width, height, x, y;
    FlipEnum flipType = FlipEnum.NONE;
    public String key;
    public Spritesheet spritesheet;
    public double pixelScale = SpriteManager.pixelScale;

    /**
     * A constructor for setting all fields without flipping and using the default pixel scale
     * @param width the width of the sprite
     * @param height the height of the sprite
     * @param x the x offset on the Spritesheet
     * @param y the y offset on the Spritesheet
     * @param key the key used for drawing the sprite
     * @param spritesheet the spritesheet that the sprites are drawn from
     */
    public SpriteInfo(int width, int height, int x, int y, String key, Spritesheet spritesheet) {
        this.spritesheet = spritesheet;
        this.key = key;
        this.width = width;;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    /**
     * A constructor for setting all fields and using the default pixel scale
     * @param width the width of the sprite
     * @param height the height of the sprite
     * @param x the x offset on the Spritesheet
     * @param y the y offset on the Spritesheet
     * @param flipType the axis that the sprite should be flipped over
     * @param key the key used for drawing the sprite
     * @param spritesheet the spritesheet that the sprites are drawn fro
     */
    public SpriteInfo(int width, int height, int x, int y, FlipEnum flipType, String key, Spritesheet spritesheet) {
        this.spritesheet = spritesheet;
        this.key = key;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.flipType = flipType;
    }

    /**
     * A constructor for setting all fields without flipping and using custom pixel scale
     * @param width
     * @param height
     * @param x
     * @param y
     * @param key
     * @param spritesheet
     * @param pixelScale
     */
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
