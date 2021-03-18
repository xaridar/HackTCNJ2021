package com.topperbibb.hacktcnj2021.client.game.graphics;

import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.user.MovableUser;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * A pane that draws parts of the game in different layers
 */
public class LevelRenderer extends JLayeredPane {


    public LevelRenderer() {
    }

    /**
     * Renders all static objects on the board, such as walls and floors
     * @return a BufferedImage with all the sprites that should be drawn
     */
    public BufferedImage renderStatic() {
        BufferedImage img = new BufferedImage(Board.board[0].length * SpriteManager.tileSize, Board.board.length * SpriteManager.tileSize, BufferedImage.TYPE_INT_ARGB);
        SpriteInfo sprite;
        int pixel;
        for (int i = 0, xo = 0; i < Board.board.length; i++, xo += SpriteManager.tileSize) {
            for (int j = 0, yo = 0; j < Board.board[i].length; j++, yo += SpriteManager.tileSize) {
                sprite = Board.board[i][j].getSprite();
                for (int x = sprite.x, imgX = 0; x < sprite.width + sprite.x; x++, imgX++) {
                    for (int y = sprite.y, imgY = 0; y < sprite.height + sprite.y; y++, imgY++) {
                        if ((imgX == 0 || imgY == 0 || imgX == sprite.width - 1 || imgY == sprite.height - 1) && sprite.key.equals("Basic_ground") && SpriteManager.tileBorders) {
                            pixel = 0xFF000000;
                        } else pixel = sprite.spritesheet.pixels[(y) * sprite.spritesheet.width + (x)];
                        img.setRGB((sprite.flipX() ? (sprite.width - 1 - imgX) + yo - ((SpriteManager.tileSize - sprite.width) / 2) : imgX + yo + ((SpriteManager.tileSize - sprite.width) / 2)), (sprite.flipY() ? (sprite.height - 1 - imgY) + xo - ((SpriteManager.tileSize - sprite.width) / 2) : imgY + xo + ((SpriteManager.tileSize - sprite.height) / 2)), pixel);
                    }
                }
            }
        }
        return img;
    }

    /**
     * Renders the player sprite with whatever direction that it should be facing
     * @param player the player that the returned BufferedImage should draw
     * @return a BufferedImage of the player with the correct sprite
     */
    public BufferedImage renderPlayer(MovableUser player) {
        SpriteInfo sprite = player.getSprite();
        BufferedImage img = new BufferedImage(sprite.width, sprite.height, BufferedImage.TYPE_INT_ARGB);
        int pixel;
        for (int x = sprite.x, imgX = 0; x < sprite.width + sprite.x; x++, imgX++) {
            for (int y = sprite.y, imgY = 0; y < sprite.height + sprite.y; y++, imgY++) {
                pixel = sprite.spritesheet.pixels[y * sprite.spritesheet.width + x];
                img.setRGB(sprite.flipX() ? sprite.width - 1 - imgX : imgX, sprite.flipY() ? sprite.height - 1 - imgY : imgY, pixel);
            }
        }
        return img;
    }

    /**
     * Renders all objects, in their own JPanel, and sets its size and offsets
     * @return an ArrayList of JPanels that contain a JLabel containing an ImageIcon of the object sprite, drawn to as close to the middle as possible
     */
    public ArrayList<JPanel> renderObjects() {
        ArrayList<JPanel> out = new ArrayList<>();

        for (int i = 0; i < Board.board.length; i++) {
            for (int j = 0; j < Board.board[i].length; j++) {
                if (Board.board[i][j].getObject() != null  && !(Board.board[i][j].getObject() instanceof MovableUser)) {
                    JPanel panel = new JPanel(new BorderLayout());
                    SpriteInfo sprite = Board.board[i][j].getObject().getSprite();
                    int pixel;
                    BufferedImage img = new BufferedImage(sprite.width, sprite.height, BufferedImage.TYPE_INT_ARGB);
                    for (int x = sprite.x, imgX = 0; x < sprite.width + sprite.x; x++, imgX++) {
                        for (int y = sprite.y, imgY = 0; y < sprite.height + sprite.y; y++, imgY++) {
                            pixel = sprite.spritesheet.pixels[(y) * sprite.spritesheet.width + (x)];
                            img.setRGB(sprite.flipX() ? sprite.width - 1 - imgX : imgX, sprite.flipY() ? sprite.height - 1 - imgY : imgY, pixel);
                        }
                    }
                    Image scaledImage = img.getScaledInstance((int) (sprite.pixelScale * sprite.width), (int) (sprite.pixelScale * sprite.height), Image.SCALE_DEFAULT);
                    panel.setBackground(new Color(0, 0, 0, 0));
                    panel.add(new JLabel(new ImageIcon(scaledImage)));
                    panel.setBounds(
                            (int) (SpriteManager.tileSize * SpriteManager.pixelScale * j + ((SpriteManager.tileSize * SpriteManager.pixelScale - sprite.pixelScale * sprite.width) / 2)),
                            (int) (SpriteManager.tileSize * SpriteManager.pixelScale * i + ((SpriteManager.tileSize * SpriteManager.pixelScale - sprite.pixelScale * sprite.height) / 2)),
                            (int) (sprite.width * sprite.pixelScale),
                            (int) (sprite.height * sprite.pixelScale)
                    );
                    out.add(panel);
                }

            }
        }
        return out;
    }

    /**
     * Renders the spawn tile drawn onto a BufferedImage
     * @return a BufferedImage containing the spawn sprite
     */
    public BufferedImage renderSpawn() {
        SpriteInfo sprite = SpriteManager.get("Spawn_point");
        BufferedImage img = new BufferedImage(sprite.width, sprite.height, BufferedImage.TYPE_INT_ARGB);
        int pixel;
        for (int x = sprite.x, imgX = 0; x < sprite.width + sprite.x; x++, imgX++) {
            for (int y = sprite.y, imgY = 0; y < sprite.height + sprite.y; y++, imgY++) {
                pixel = sprite.spritesheet.pixels[y * sprite.spritesheet.width + x];
                img.setRGB(imgX, imgY, pixel);
            }
        }
        return img;
    }

    /**
     * Renders the end tile drawn onto a BufferedImage
     * @return a BufferedImage containing the end sprite
     */
    public BufferedImage renderEnd() {
        SpriteInfo sprite = SpriteManager.get("End");
        BufferedImage img = new BufferedImage(sprite.width, sprite.height, BufferedImage.TYPE_INT_ARGB);
        int pixel;
        for (int x = sprite.x, imgX = 0; x < sprite.width + sprite.x; x++, imgX++) {
            for (int y = sprite.y, imgY = 0; y < sprite.height + sprite.y; y++, imgY++) {
                pixel = sprite.spritesheet.pixels[y * sprite.spritesheet.width + x];
                img.setRGB(imgX, imgY, pixel);
            }
        }
        return img;
    }
}
