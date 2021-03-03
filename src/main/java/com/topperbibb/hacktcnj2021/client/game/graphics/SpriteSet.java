package com.topperbibb.hacktcnj2021.client.game.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpriteSet {
    private Random random;
    private int index;
    private int count;
    private SpriteInfo nextSprite;
    public String key;
    private boolean randomized;

    private final List<SpriteInfo> sprites;

    public SpriteSet(String key) {
        this.key = key;
        sprites = new ArrayList<>();
        index = 0;
        nextSprite = null;
        count = 0;
    }

    public SpriteSet(List<SpriteInfo> sprites, String key) {
        this.key = key;
        this.sprites = new ArrayList<>(sprites);
        index = 0;
        nextSprite = sprites.get(index);
        count = sprites.size();
    }

    public SpriteSet(SpriteInfo sprite) {
        this.key = sprite.key;
        this.sprites = new ArrayList<>();
        sprites.add(sprite);
        index = 0;
        nextSprite = sprite;
        count = 1;
    }

    public SpriteSet(String key, boolean randomized) {
        this.randomized = randomized;
        random = new Random();
        this.key = key;
        sprites = new ArrayList<>();
        index = 0;
        nextSprite = null;
        count = 0;
    }

    public SpriteSet(List<SpriteInfo> sprites, String key, boolean randomized) {
        this.randomized = randomized;
        random = new Random();
        this.key = key;
        this.sprites = new ArrayList<>(sprites);
        index = 0;
        nextSprite = sprites.get(index);
        count = sprites.size();
    }

    public SpriteSet(SpriteInfo sprite, boolean randomized) {
        this.randomized = randomized;
        random = new Random();
        this.key = sprite.key;
        this.sprites = new ArrayList<>();
        sprites.add(sprite);
        index = 0;
        nextSprite = sprite;
        count = 1;
    }

    public SpriteSet added(SpriteInfo sprite) {
        count++;
        sprites.add(sprite);
        if (nextSprite == null) {
            nextSprite = sprite;
        }
        return this;
    }

    public SpriteSet added(SpriteInfo sprite, int pos) throws IndexOutOfBoundsException {
        if (pos > count) throw new IndexOutOfBoundsException(pos);
        count++;
        sprites.add(pos, sprite);
        if (nextSprite == null) {
            nextSprite = sprite;
        }
        return this;
    }

    public SpriteSet removed(SpriteInfo sprite) {
        count--;
        sprites.remove(sprite);
        return this;
    }

    public SpriteInfo next() {
        SpriteInfo next = sprites.get(index);
        index++;
        if (randomized) index = random.nextInt(count);
        if (index >= count) {
            index = 0;
        }
        nextSprite = sprites.get(index);
        return next;
    }

    public SpriteInfo last() {
        SpriteInfo next = sprites.get(index);
        index--;
        nextSprite = sprites.get(index);
        return next;
    }

    public int size() {
        return count;
    }
}
