package com.topperbibb.hacktcnj2021.client.game.graphics;

import java.awt.image.BufferedImage;

public class SpriteRenderer {

    public BufferedImage renderSprite(SpriteInfo sprite) {
        BufferedImage img = new BufferedImage(sprite.size, sprite.size, BufferedImage.TYPE_INT_ARGB);
        for (int x = sprite.x, imgX = 0; x < sprite.size + sprite.x; x++, imgX++) {
            for (int y = sprite.y, imgY = 0; y < sprite.size + sprite.y; y++, imgY++) {
                int pixel = sprite.sheet.pixels[y * sprite.sheet.width + x];
                img.setRGB(imgX, imgY, pixel);
            }
        }
        return img;
    }

}
