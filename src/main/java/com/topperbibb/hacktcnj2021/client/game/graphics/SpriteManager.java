package com.topperbibb.hacktcnj2021.client.game.graphics;

import java.util.HashMap;
import java.util.Map;

/**
 * Adds new sprites to a pool of sprites with keys and SpriteSets, so that they can be used by used universally
 */
public class SpriteManager {
    public static double pixelScale = 4;
    private static final Map<String, SpriteSet> sprites = new HashMap<>();
    public static int tileSize;
    public static Spritesheet spritesheet;
    public static boolean tileBorders = false;

    /**
     * Adds a sprite to the map in the form of a SpriteInfo instance, as long as there aren't duplicates.
     * @param sprite The SpriteInfo to add to the map
     */
    public static void addSprite(SpriteInfo sprite) {
        if (!sprites.containsKey(sprite.key)) {
            sprites.put(sprite.key, new SpriteSet(sprite));
        } else {
            sprites.get(sprite.key).added(sprite);
        }
    }

    /**
     * Adds a
     * @param sprite
     * @param pos
     * @throws IndexOutOfBoundsException
     */
    public static void addSprite(SpriteInfo sprite, int pos) throws IndexOutOfBoundsException {
        if (!sprites.containsKey(sprite.key)) {
            sprites.put(sprite.key, new SpriteSet(sprite));
        } else {
            sprites.get(sprite.key).added(sprite, pos);
        }
    }

    public static void addSprite(SpriteSet set) throws IndexOutOfBoundsException {
        sprites.put(set.key, set);
    }

    public static void addSpriteDefault(SpriteInfo sprite) {
        sprites.putIfAbsent(sprite.key, new SpriteSet(sprite));
    }

    public static void addSpriteDefault(SpriteSet set) throws IndexOutOfBoundsException {
        sprites.putIfAbsent(set.key, set);
    }

    public static SpriteInfo get(String name) {
        return sprites.get(name).next();
    }
}
