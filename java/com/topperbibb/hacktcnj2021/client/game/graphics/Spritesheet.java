package com.topperbibb.hacktcnj2021.client.game.graphics;

import com.topperbibb.hacktcnj2021.client.game.Engine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Spritesheet {

    public int width;
    public int height;

    public int size;

    public int sizeSprites;

    public int[] pixels;

    public Spritesheet(String path, int size) {
        this.size = size;

        BufferedImage image;

        try {
            image = ImageIO.read(Engine.class.getResourceAsStream(path));
        } catch (IOException e) {
            throw new Error(e);
        }

        this.width = image.getWidth();
        this.height = image.getHeight();
        this.pixels = new int[this.width * this.height];

        assert (this.width == this.height);
        this.sizeSprites = this.width / this.size;

        pixels = image.getRGB(
                0, 0, image.getWidth(), image.getHeight(),
                null, 0, image.getWidth());

    }
}