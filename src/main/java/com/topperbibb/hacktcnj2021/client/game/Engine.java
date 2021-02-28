package com.topperbibb.hacktcnj2021.client.game;

import com.topperbibb.hacktcnj2021.client.game.graphics.FlipEnum;
import com.topperbibb.hacktcnj2021.client.game.user.MovableUser;
import com.topperbibb.hacktcnj2021.client.game.graphics.LevelRenderer;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;
import com.topperbibb.hacktcnj2021.client.game.graphics.Spritesheet;
import com.topperbibb.hacktcnj2021.client.game.levels.Level;
import com.topperbibb.hacktcnj2021.client.game.levels.TestLevel;
import com.topperbibb.hacktcnj2021.client.game.user.NetUser;
import com.topperbibb.hacktcnj2021.client.game.user.StaticUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Engine implements KeyListener, MouseListener {

    private NetUser localUser;

    private Level currLevel;
    private Spritesheet spritesheet;
    private LevelRenderer renderer;
    private JFrame window;
    private JPanel levelPanel;
    private JPanel playerPanel;
    private JPanel spawnPanel;
    private ArrayList<JPanel> objectPanels;
    public static final long keyTimeout = 300;
    private long keyPressed;
    private MovableUser.PlayerSprite lastDir;


    private static Engine INSTANCE;

    public static Engine getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Engine();
        }

        return INSTANCE;
    }

    public Engine() {



        Map<MovableUser.PlayerSprite, SpriteInfo> playerSprites = new HashMap<>();
        playerSprites.put(MovableUser.PlayerSprite.RIGHT, new SpriteInfo(16, 32, 88));
        playerSprites.put(MovableUser.PlayerSprite.RIGHT2, new SpriteInfo(16, 48, 88));
        playerSprites.put(MovableUser.PlayerSprite.LEFT, new SpriteInfo(16, 32, 88, FlipEnum.X));
        playerSprites.put(MovableUser.PlayerSprite.LEFT2, new SpriteInfo(16, 48, 88, FlipEnum.X));
        playerSprites.put(MovableUser.PlayerSprite.UP, new SpriteInfo(16, 16, 88));
        playerSprites.put(MovableUser.PlayerSprite.UP2, new SpriteInfo(16, 16, 88, FlipEnum.X));
        playerSprites.put(MovableUser.PlayerSprite.DOWN, new SpriteInfo(16, 0, 88));
        playerSprites.put(MovableUser.PlayerSprite.DOWN2, new SpriteInfo(16, 0, 88, FlipEnum.X));
        currLevel = new TestLevel(new MovableUser(1, 1, playerSprites), new StaticUser());
        localUser = currLevel.getMovableUser();
        spritesheet = new Spritesheet("/tiles.png");
    }

    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.createRenderWindow();
        engine.renderStaticTiles(16, 4);
        engine.renderPlayer(engine.currLevel.getMovableUser(), MovableUser.PlayerSprite.RIGHT);
        engine.renderSpawn(16, 4);
        engine.renderPlayer(engine.currLevel.getMovableUser(), MovableUser.PlayerSprite.RIGHT);
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
        levelPanel.setBackground(new Color(0, 0, 0, 0));
        levelPanel.setBounds(0, 0, Board.board[0].length * 16 * 4, Board.board.length * 16 * 4);
        renderer.add(levelPanel, JLayeredPane.DEFAULT_LAYER);

        playerPanel = new JPanel(new BorderLayout());
        playerPanel.setBackground(new Color(0, 0, 0, 0));
        playerPanel.setBounds(0, 0, 16, 16);
        renderer.add(playerPanel, JLayeredPane.DRAG_LAYER);

        spawnPanel = new JPanel(new BorderLayout());
        spawnPanel.setBackground(new Color(0, 0, 0, 0));
        spawnPanel.setBounds(16 * 4 * Board.getSpawnTile(Board.board).getX(), 16 * 4 * Board.getSpawnTile(Board.board).getY(), 16, 16);
        renderer.add(spawnPanel, JLayeredPane.PALETTE_LAYER);

        renderer.addKeyListener(this);
        renderer.addMouseListener(this);
        renderer.setFocusable(true);
        renderer.requestFocusInWindow();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
    }

    public void renderStaticTiles(int spriteSize, int scale) {
        Image img = renderer.renderStatic(Board.board[0].length * spriteSize, Board.board.length * spriteSize, spriteSize);
        img = img.getScaledInstance(Board.board[0].length * spriteSize * scale, Board.board.length * spriteSize * scale, Image.SCALE_DEFAULT);
        levelPanel.removeAll();
        levelPanel.add(new JLabel(new ImageIcon(img)));
        window.revalidate();
        window.pack();
    }

    public void renderPlayer(MovableUser player, MovableUser.PlayerSprite direction) {
        Image img = renderer.renderPlayer(player, 16, 16, direction);
        img = img.getScaledInstance(16 * 4, 16 * 4, Image.SCALE_DEFAULT);
        playerPanel.removeAll();
        playerPanel.add(new JLabel(new ImageIcon(img)));
        playerPanel.setBounds(4 * 16 * player.x, 4 * 16 * player.y, 16 * 4, 16 * 4);
        window.revalidate();
        window.pack();
    }

    public void renderObjects(int spriteSize, int scale) {
        if (objectPanels != null) {
            for (JPanel object : objectPanels) {
                renderer.remove(object);
            }
        }
        objectPanels = renderer.renderObjects(spriteSize, scale);

        for (JPanel object : objectPanels) {
            renderer.add(object, JLayeredPane.MODAL_LAYER);
        }

        renderer.remove(spawnPanel);
        renderer.add(spawnPanel, JLayeredPane.PALETTE_LAYER);

        renderer.remove(playerPanel);
        renderer.add(playerPanel, JLayeredPane.DRAG_LAYER);

        window.revalidate();
    }

    public void renderSpawn(int spriteSize, int scale) {
        Image img = renderer.renderSpawn(spriteSize, scale);
        img = img.getScaledInstance(spriteSize * scale, spriteSize * scale, Image.SCALE_DEFAULT);
        spawnPanel.removeAll();
        spawnPanel.add(new JLabel(new ImageIcon(img)));
        spawnPanel.setBounds(scale * spriteSize * Board.getSpawnTile(Board.board).getX(), scale * spriteSize * Board.getSpawnTile(Board.board).getY(), spriteSize * scale, spriteSize * scale);

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
        if (localUser instanceof MovableUser) {
            MovableUser user = (MovableUser) localUser;
            switch (e.getKeyChar()) {
                case 'W':
                case 'w':
                    if (keyPressed + keyTimeout <= System.currentTimeMillis()) {
                        if (user.move(0, -1)) {
                            MovableUser.PlayerSprite dir = lastDir == MovableUser.PlayerSprite.UP ? MovableUser.PlayerSprite.UP2 : MovableUser.PlayerSprite.UP;
                            renderPlayer(user, dir);
                            renderObjects(16, 4);
                            lastDir = dir;
                        }
                        keyPressed = System.currentTimeMillis();
                    }
                    break;
                case 'S':
                case 's':
                    if (keyPressed + keyTimeout <= System.currentTimeMillis()) {
                        if (user.move(0, 1)) {
                            MovableUser.PlayerSprite dir = lastDir == MovableUser.PlayerSprite.DOWN ? MovableUser.PlayerSprite.DOWN2 : MovableUser.PlayerSprite.DOWN;
                            renderPlayer(user, dir);
                            renderObjects(16, 4);
                            lastDir = dir;
                        }
                        keyPressed = System.currentTimeMillis();
                    }
                    break;
                case 'A':
                case 'a':
                    if (keyPressed + keyTimeout <= System.currentTimeMillis()) {
                        if (user.move(-1, 0)) {
                            MovableUser.PlayerSprite dir = lastDir == MovableUser.PlayerSprite.LEFT ? MovableUser.PlayerSprite.LEFT2 : MovableUser.PlayerSprite.LEFT;
                            renderPlayer(user, dir);
                            renderObjects(16, 4);
                            lastDir = dir;
                        }
                        keyPressed = System.currentTimeMillis();
                    }
                    break;
                case 'D':
                case 'd':
                    if (keyPressed + keyTimeout <= System.currentTimeMillis()) {
                        if (user.move(1, 0)) {
                            MovableUser.PlayerSprite dir = lastDir == MovableUser.PlayerSprite.RIGHT ? MovableUser.PlayerSprite.RIGHT2 : MovableUser.PlayerSprite.RIGHT;
                            renderPlayer(user, dir);
                            renderObjects(16, 4);
                            lastDir = dir;
                        }
                        break;
                    }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPressed = 0;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(localUser instanceof StaticUser) {
            int x = e.getX();
            int y = e.getY();
            System.out.println((x / 64) + ", " + (y / 64));
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
