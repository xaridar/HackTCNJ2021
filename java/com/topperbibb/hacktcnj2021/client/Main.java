package com.topperbibb.hacktcnj2021.client;

import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;
import com.topperbibb.hacktcnj2021.client.game.graphics.LevelRenderer;
import com.topperbibb.hacktcnj2021.client.game.graphics.Spritesheet;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Spritesheet sheet = new Spritesheet("/tiles.png");
        SpriteInfo topLeftInfo = new SpriteInfo(16, 96, 48);
        JFrame frame = new JFrame();
        JPanel panel = new JPanel(new BorderLayout());
        frame.add(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
