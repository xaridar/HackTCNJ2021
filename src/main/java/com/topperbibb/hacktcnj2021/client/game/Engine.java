package com.topperbibb.hacktcnj2021.client.game;

import com.topperbibb.hacktcnj2021.client.User;
import com.topperbibb.hacktcnj2021.client.game.graphics.LevelRenderer;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;
import com.topperbibb.hacktcnj2021.client.game.graphics.Spritesheet;
import com.topperbibb.hacktcnj2021.client.game.levels.Level;
import com.topperbibb.hacktcnj2021.client.game.levels.TestLevel;
import com.topperbibb.hacktcnj2021.client.game.util.PlayerKeyEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Engine implements KeyListener {

    private Level currLevel;
    private Spritesheet spritesheet;
    private LevelRenderer renderer;
    private JFrame window;
    private JPanel levelPanel;
    private JPanel playerPanel;
    private JPanel spawnPanel;
    private JPanel objectPanel;


    private static Engine INSTANCE;

    public static Engine getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Engine();
        }

        return INSTANCE;
    }

    public Engine() {
        currLevel = new TestLevel(new User(1, 1, new SpriteInfo(16, 32, 88)));
        spritesheet = new Spritesheet("/tiles.png");
    }

    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.createRenderWindow();
        engine.renderStaticTiles(16, 4);
        engine.renderPlayer(engine.currLevel.getPlayer());
        engine.renderObjects(16, 4);
    }

    public void createRenderWindow() {
        window = new JFrame();
        window.setPreferredSize(new Dimension(Board.board[0].length * 16 * 4 + 16, Board.board.length * 16 * 4 + 38));
        window.setLayout(new BorderLayout());
        renderer = new LevelRenderer(spritesheet);
        window.add(renderer, BorderLayout.CENTER);
        renderer.setBounds(0, 0, Board.board[0].length * 16 * 4, Board.board.length * 16 * 4);

        levelPanel = new JPanel(new BorderLayout());
        levelPanel.setBackground(Color.BLACK);
        levelPanel.setBounds(0, 0, Board.board[0].length * 16 * 4, Board.board.length * 16 * 4);
        renderer.add(levelPanel, 1000);

//        spawnPanel = new JPanel(new BorderLayout());
//        spawnPanel.setBackground(new Color(0, 0, 0, 0));
//        spawnPanel.setBounds(0, 0, 16, 16);
//        renderer.add(spawnPanel, 0, 2);

        playerPanel = new JPanel(new BorderLayout());
        playerPanel.setBackground(new Color(0, 0, 0, 0));
        playerPanel.setBounds(0, 0, 16, 16);
        renderer.add(playerPanel, 1);

//        objectPanel = new JPanel(new BorderLayout());
//        objectPanel.setBackground(new Color(0, 0, 0, 0));
//        objectPanel.setBounds(0, 0, Board.board[0].length * 16 * 4, Board.board.length * 16 * 4);
//        renderer.add(objectPanel, 0);


        renderer.addKeyListener(this);
        renderer.setFocusable(true);
        renderer.requestFocusInWindow();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
    }

    public void renderStaticTiles(int spriteSize, int scale) {
        Image img = renderer.renderStatic(Board.board[0].length * spriteSize, Board.board.length * spriteSize, spriteSize);
        img = img.getScaledInstance(Board.board[0].length * spriteSize * scale, Board.board.length * spriteSize * scale, Image.SCALE_DEFAULT);
        levelPanel.add(new JLabel(new ImageIcon(img)));
        window.revalidate();
        window.pack();
    }

    public void renderPlayer(User player) {
        Image img = renderer.renderPlayer(player, 16, 16);
        img = img.getScaledInstance(16 * 4, 16 * 4, Image.SCALE_DEFAULT);
        playerPanel.add(new JLabel(new ImageIcon(img)));
        playerPanel.setBounds(4 * 16 * player.x, 4 * 16 * player.y, 16 * 4, 16 * 4);
        window.revalidate();
        window.pack();
    }

    public void renderObjects(int spriteSize, int scale) {
        ArrayList<JPanel> objects = renderer.renderObjects(spriteSize, scale);
        int index = 0;
        for (JPanel object : objects) {
            renderer.add(object, index);
            index++;
        }
        renderer.add(playerPanel, index);
//        index++;
//        renderer.add(levelPanel, index);
//
        window.revalidate();
        window.pack();
    }

    public Level getCurrLevel() {
        return currLevel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyChar()){
            case 'W':
            case 'w':
                if(currLevel.getPlayer().move(0, -1)) {
                    renderPlayer(currLevel.getPlayer());
                }
                break;
            case 'S':
            case 's':
                if(currLevel.getPlayer().move(0, 1)) {
                    renderPlayer(currLevel.getPlayer());
                }
                break;
            case 'A':
            case 'a':
                if(currLevel.getPlayer().move(-1, 0)) {
                    renderPlayer(currLevel.getPlayer());
                }
                break;
            case 'D':
            case 'd':
                if(currLevel.getPlayer().move(1, 0)) {
                    renderPlayer(currLevel.getPlayer());
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
