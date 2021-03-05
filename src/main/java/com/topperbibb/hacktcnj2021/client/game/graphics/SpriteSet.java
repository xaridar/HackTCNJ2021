package com.topperbibb.hacktcnj2021.client.game.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A set of sprites for one object (i.e. player animations, object textures)
 */
public class SpriteSet {
    private Random random;
    private int index;
    private int count;
    private SpriteInfo nextSprite;
    public String key;
    private boolean randomized;

    private final List<SpriteInfo> sprites;

    /**
     * Initializes a spriteset using a key
     * @param key an identifier used in the SpriteManager
     */
    public SpriteSet(String key) {
        this.key = key;
        sprites = new ArrayList<>();
        index = 0;
        nextSprite = null;
        count = 0;
    }

    /**
     * Creates a SpriteSet using a list of sprites and a key
     * @param sprites the sprites that will be used in the set
     * @param key the identifier used in the SpriteManager
     */
    public SpriteSet(List<SpriteInfo> sprites, String key) {
        this.key = key;
        this.sprites = new ArrayList<>(sprites);
        index = 0;
        nextSprite = sprites.get(index);
        count = sprites.size();
    }

    /**
     * Creates a SpriteSet that has one static sprite
     * @param sprite the info for the sprite that makes up this set
     */
    public SpriteSet(SpriteInfo sprite) {
        this.key = sprite.key;
        this.sprites = new ArrayList<>();
        sprites.add(sprite);
        index = 0;
        nextSprite = sprite;
        count = 1;
    }

    /**
     * Creates a spriteset with a list of sprites and a key that pulls random sprites when it is drawn
     * @param sprites the sprites to be drawn
     * @param key the key to identify the set
     * @param randomized whether or not the sprites drawn are pulled randomly from the set
     */
    public SpriteSet(List<SpriteInfo> sprites, String key, boolean randomized) {
        this.randomized = randomized;
        random = new Random();
        this.key = key;
        this.sprites = new ArrayList<>(sprites);
        index = 0;
        nextSprite = sprites.get(index);
        count = sprites.size();
    }

    /**
     * Adds the given sprite to the list of possible sprites and sets it as the next sprite if there are no next sprites.
     * @param sprite the sprite to add
     * @return the new spriteset with the added sprite
     */
    public SpriteSet added(SpriteInfo sprite) {
        count++;
        sprites.add(sprite);
        if (nextSprite == null) {
            nextSprite = sprite;
        }
        return this;
    }

    /**
     * Adds the given sprite at the given position
     * @param sprite the sprite to add
     * @param pos the position to set the sprite at
     * @return the new SpriteSet
     * @throws IndexOutOfBoundsException thrown if the position given is greater than the size of the SpriteSet
     */
    public SpriteSet added(SpriteInfo sprite, int pos) throws IndexOutOfBoundsException {
        if (pos > count) throw new IndexOutOfBoundsException(pos);
        count++;
        sprites.add(pos, sprite);
        if (nextSprite == null) {
            nextSprite = sprite;
        }
        return this;
    }

    /**
     * Removes the given sprite
     * @param sprite to remove
     * @return the new SpriteSet
     */
    public SpriteSet removed(SpriteInfo sprite) {
        count--;
        sprites.remove(sprite);
        return this;
    }

    /**
     * Gets the next sprite from the sprite list
     * @return SpriteInfo about the next sprite
     */
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
