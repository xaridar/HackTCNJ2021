package com.topperbibb.hacktcnj2021.client.game.graphics;

import com.topperbibb.hacktcnj2021.client.User;
import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;

public class LevelRenderer extends JLayeredPane {

    Spritesheet sheet;

    public LevelRenderer(Spritesheet sheet) {
        this.sheet = sheet;
    }

    public BufferedImage renderStatic(int width, int height, int spriteSize) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        SpriteInfo sprite;
        int pixel;
        for (int i = 0, xo = 0; i < Board.board.length; i++, xo += spriteSize) {
            for (int j = 0, yo = 0; j < Board.board[i].length; j++, yo += spriteSize) {
                sprite = Board.board[i][j].getSprite();
                for (int x = sprite.x, imgX = 0; x < sprite.size + sprite.x; x++, imgX++) {
                    for (int y = sprite.y, imgY = 0; y < sprite.size + sprite.y; y++, imgY++) {
                        pixel = sheet.pixels[(y) * sheet.width + (x)];
                        img.setRGB(imgY + yo, imgX + xo, pixel);
                    }
                }
            }
        }
        return img;
    }

    public BufferedImage renderPlayer(User player, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        SpriteInfo sprite = player.getSprite();
        int pixel;
        for (int x = sprite.x, imgX = 0; x < sprite.size + sprite.x; x++, imgX++) {
            for (int y = sprite.y, imgY = 0; y < sprite.size + sprite.y; y++, imgY++) {
                pixel = sheet.pixels[y * sheet.width + x];
                img.setRGB(imgX, imgY, pixel);
            }
        }
        return img;
    }

    public ArrayList<JPanel> renderObjects(int spriteSize, int scale) {
        ArrayList<JPanel> out = new ArrayList<>();

        for (int i = 0; i < Board.board.length; i++) {
            for (int j = 0; j < Board.board[i].length; j++) {
                if (Board.board[i][j].getObject() != null) {
                    JPanel panel = new JPanel(new BorderLayout());
                    SpriteInfo sprite = Board.board[i][j].getObject().getSprite();
                    int pixel;
                    BufferedImage img = new BufferedImage(spriteSize, spriteSize, BufferedImage.TYPE_INT_ARGB);
                    for (int x = sprite.x, imgX = 0; x < sprite.size + sprite.x; x++, imgX++) {
                        for (int y = sprite.y, imgY = 0; y < sprite.size + sprite.y; y++, imgY++) {
                            pixel = sheet.pixels[(y) * sheet.width + (x)];
                            img.setRGB(imgY, imgX, pixel);
                        }
                    }
                    Image scaledImage = img.getScaledInstance(scale * spriteSize, scale * spriteSize, Image.SCALE_DEFAULT);
                    panel.setBackground(new Color(0, 0, 0, 0));
                    panel.add(new JLabel(new ImageIcon(scaledImage)));
                    panel.setBounds(spriteSize * scale * j, spriteSize * scale * i, spriteSize * scale, spriteSize * scale);
                    out.add(panel);
                }

            }
        }
        System.out.println(out);
        return out;
    }
}
