package com.topperbibb.hacktcnj2021.client.game.graphics;

import java.util.HashMap;
import java.util.Map;

public class SpriteManager {
    private static final Map<String, SpriteInfo> sprites = new HashMap<>();
    public static int defaultSpriteSize;

    public static void addSprite(SpriteInfo sprite) {
        sprites.put(sprite.key, sprite);
    }

    public static SpriteInfo get(String name) {
        return sprites.get(name);
    }
}
