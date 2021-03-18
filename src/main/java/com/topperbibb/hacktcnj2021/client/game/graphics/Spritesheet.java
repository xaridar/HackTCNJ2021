package com.topperbibb.hacktcnj2021.client.game.graphics;

import com.topperbibb.hacktcnj2021.client.game.Engine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Spritesheet {

    public int width;
    public int height;

    public int[] pixels;

    public Spritesheet(String path) {

        BufferedImage image;

        try {
            InputStream stream = Engine.class.getResourceAsStream("/" + path);
            if (stream != null) {
                image = ImageIO.read(stream);
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            System.out.println("Spritesheet not found: " + path);
            System.exit(0);
            return;
        }

        this.width = image.getWidth();
        this.height = image.getHeight();
        this.pixels = new int[this.width * this.height];

        assert (this.width == this.height);

        pixels = image.getRGB(
                0, 0, image.getWidth(), image.getHeight(),
                null, 0, image.getWidth());

    }
}