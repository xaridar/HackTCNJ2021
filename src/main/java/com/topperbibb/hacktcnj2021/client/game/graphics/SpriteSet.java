package com.topperbibb.hacktcnj2021.client.game.graphics;

import com.topperbibb.hacktcnj2021.client.game.util.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class SpriteSet {
    private Random random;
    private int index;
    private int count;
    private SpriteInfo nextSprite;
    public String key;
    private boolean randomized;

    private final List<Tuple<Double, SpriteInfo>> sprites;

    public SpriteSet(String key) {
        this.key = key;
        sprites = new ArrayList<>();
        index = 0;
        nextSprite = null;
        count = 0;
    }

    public SpriteSet(List<SpriteInfo> sprites, String key) {
        this.key = key;
        this.sprites = sprites.stream().map(spr -> Tuple.of(1d, spr)).collect(Collectors.toList());
        index = 0;
        nextSprite = sprites.get(index);
        count = sprites.size();
    }

    public SpriteSet(SpriteInfo sprite) {
        this.key = sprite.key;
        this.sprites = new ArrayList<>();
        sprites.add(Tuple.of(1d, sprite));
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

    public SpriteSet(List<Tuple<Double, SpriteInfo>> sprites, String key, boolean randomized) {
        this.randomized = randomized;
        random = new Random();
        this.key = key;
        this.sprites = new ArrayList<>(sprites);
        index = 0;
        nextSprite = sprites.get(index).getRight();
        count = sprites.size();
    }

    public SpriteSet(SpriteInfo sprite, double prob, boolean randomized) {
        this.randomized = randomized;
        random = new Random();
        this.key = sprite.key;
        this.sprites = new ArrayList<>();
        sprites.add(Tuple.of(prob, sprite));
        index = 0;
        nextSprite = sprite;
        count = 1;
    }

    public SpriteSet added(SpriteInfo sprite) {
        count++;
        sprites.add(Tuple.of(1d, sprite));
        if (nextSprite == null) {
            nextSprite = sprite;
        }
        return this;
    }

    public SpriteSet added(double prob, SpriteInfo sprite) {
        count++;
        sprites.add(Tuple.of(prob, sprite));
        if (nextSprite == null) {
            nextSprite = sprite;
        }
        return this;
    }

    public SpriteSet added(SpriteInfo sprite, int pos) throws IndexOutOfBoundsException {
        if (pos > count) throw new IndexOutOfBoundsException(pos);
        count++;
        sprites.add(pos, Tuple.of(1d, sprite));
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
        SpriteInfo next = sprites.get(index).getRight();
        index++;
        if (randomized) {
            double i = random.nextDouble() * sprites.stream().map(Tuple::getLeft).reduce(Double::sum).orElse(0d);
            double tot = 0;
            for (int n = 0; n < sprites.size(); n++) {
                if (tot + sprites.get(n).getLeft() >= i) {
                    index = n;
                    break;
                }
                tot += sprites.get(n).getLeft();
            }
        }
        if (index >= count) {
            index = 0;
        }
        nextSprite = sprites.get(index).getRight();
        return next;
    }

    public SpriteInfo last() {
        SpriteInfo next = sprites.get(index).getRight();
        index--;
        nextSprite = sprites.get(index).getRight();
        return next;
    }

    public int size() {
        return count;
    }
}
