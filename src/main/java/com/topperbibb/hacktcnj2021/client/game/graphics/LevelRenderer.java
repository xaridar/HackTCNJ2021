package com.topperbibb.hacktcnj2021.client.game.graphics;

import com.topperbibb.hacktcnj2021.client.game.Engine;
import com.topperbibb.hacktcnj2021.client.game.user.MovableUser;
import com.topperbibb.hacktcnj2021.client.game.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelRenderer extends JLayeredPane {

    Spritesheet sheet;

    public LevelRenderer(Spritesheet sheet) {
        this.sheet = sheet;
    }

    public BufferedImage renderStatic() {
        BufferedImage img = new BufferedImage(Board.board[0].length * SpriteManager.defaultSpriteSize, Board.board.length * SpriteManager.defaultSpriteSize, BufferedImage.TYPE_INT_ARGB);
        SpriteInfo sprite;
        int pixel;
        for (int i = 0, xo = 0; i < Board.board.length; i++, xo += SpriteManager.defaultSpriteSize) {
            for (int j = 0, yo = 0; j < Board.board[i].length; j++, yo += SpriteManager.defaultSpriteSize) {
                sprite = Board.board[i][j].getSprite();
                for (int x = sprite.x, imgX = 0; x < sprite.size + sprite.x; x++, imgX++) {
                    for (int y = sprite.y, imgY = 0; y < sprite.size + sprite.y; y++, imgY++) {
                        pixel = sheet.pixels[(y) * sheet.width + (x)];
                        img.setRGB(sprite.flipX() ? (sprite.size - 1 - imgX) + yo : imgX + yo, sprite.flipY() ? (sprite.size - 1 - imgY) + yo : imgY + xo, pixel);
                    }
                }
            }
        }
        return img;
    }

    public BufferedImage renderPlayer(MovableUser player, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        SpriteInfo sprite = player.getSprite();
        int pixel;
        for (int x = sprite.x, imgX = 0; x < sprite.size + sprite.x; x++, imgX++) {
            for (int y = sprite.y, imgY = 0; y < sprite.size + sprite.y; y++, imgY++) {
                pixel = sheet.pixels[y * sheet.width + x];
                img.setRGB(sprite.flipX() ? sprite.size - 1 - imgX : imgX, sprite.flipY() ? sprite.size - 1 - imgY : imgY, pixel);
            }
        }
        return img;
    }

    public ArrayList<JPanel> renderObjects() {
        ArrayList<JPanel> out = new ArrayList<>();

        for (int i = 0; i < Board.board.length; i++) {
            for (int j = 0; j < Board.board[i].length; j++) {
                if (Board.board[i][j].getObject() != null  && !(Board.board[i][j].getObject() instanceof MovableUser)) {
                    JPanel panel = new JPanel(new BorderLayout());
                    SpriteInfo sprite = Board.board[i][j].getObject().getSprite();
                    int pixel;
                    BufferedImage img = new BufferedImage(sprite.size, sprite.size, BufferedImage.TYPE_INT_ARGB);
                    for (int x = sprite.x, imgX = 0; x < sprite.size + sprite.x; x++, imgX++) {
                        for (int y = sprite.y, imgY = 0; y < sprite.size + sprite.y; y++, imgY++) {
                            pixel = sheet.pixels[(y) * sheet.width + (x)];
                            img.setRGB(sprite.flipX() ? sprite.size - 1 - imgX : imgX, sprite.flipY() ? sprite.size - 1 - imgY : imgY, pixel);
                        }
                    }
                    Image scaledImage = img.getScaledInstance(Engine.PIXEL_SCALE * sprite.size, Engine.PIXEL_SCALE * sprite.size, Image.SCALE_DEFAULT);
                    panel.setBackground(new Color(0, 0, 0, 0));
                    panel.add(new JLabel(new ImageIcon(scaledImage)));
                    panel.setBounds(Engine.TILE_SIZE * Engine.PIXEL_SCALE * j + (Engine.TILE_SIZE/sprite.size) * Engine.PIXEL_SCALE, Engine.TILE_SIZE * Engine.PIXEL_SCALE * i + (Engine.TILE_SIZE/sprite.size) * Engine.PIXEL_SCALE, sprite.size * Engine.PIXEL_SCALE, sprite.size * Engine.PIXEL_SCALE);

                    out.add(panel);
                }

            }
        }
        return out;
    }

    public BufferedImage renderSpawn() {
        SpriteInfo sprite = SpriteManager.get("Spawn_point");
        BufferedImage img = new BufferedImage(sprite.size, sprite.size, BufferedImage.TYPE_INT_ARGB);
        int pixel;
        for (int x = sprite.x, imgX = 0; x < sprite.size + sprite.x; x++, imgX++) {
            for (int y = sprite.y, imgY = 0; y < sprite.size + sprite.y; y++, imgY++) {
                pixel = sheet.pixels[y * sheet.width + x];
                img.setRGB(imgX, imgY, pixel);
            }
        }
        return img;
    }

    public BufferedImage renderEnd() {
        SpriteInfo sprite = SpriteManager.get("End");
        BufferedImage img = new BufferedImage(sprite.size, sprite.size, BufferedImage.TYPE_INT_ARGB);
        int pixel;
        for (int x = sprite.x, imgX = 0; x < sprite.size + sprite.x; x++, imgX++) {
            for (int y = sprite.y, imgY = 0; y < sprite.size + sprite.y; y++, imgY++) {
                pixel = sheet.pixels[y * sheet.width + x];
                img.setRGB(imgX, imgY, pixel);
            }
        }
        return img;
    }
}
