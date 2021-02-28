package com.topperbibb.hacktcnj2021.client;

import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteRenderer;
import com.topperbibb.hacktcnj2021.client.game.graphics.Spritesheet;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Spritesheet sheet = new Spritesheet("/tiles.png");
        SpriteInfo topLeftInfo = new SpriteInfo(sheet, 16, 96, 48);
        Image img = new SpriteRenderer().renderSprite(topLeftInfo);
        JFrame frame = new JFrame();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(new ImageIcon(img)), BorderLayout.CENTER);
        frame.add(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
