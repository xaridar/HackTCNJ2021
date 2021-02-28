package com.topperbibb.hacktcnj2021.client.game;

import com.topperbibb.hacktcnj2021.client.game.graphics.FlipEnum;
import com.topperbibb.hacktcnj2021.client.game.tiles.Tile;
import com.topperbibb.hacktcnj2021.client.game.tiles.TileInfo;
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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Engine implements KeyListener, MouseListener {

    private NetUser localUser;

    public static final int PIXEL_SCALE = 4;
    public static final int TILE_SIZE = 16;

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
        playerSprites.put(MovableUser.PlayerSprite.RIGHT, SpriteInfo.sprites.get("Player_right_1"));
        playerSprites.put(MovableUser.PlayerSprite.RIGHT2, SpriteInfo.sprites.get("Player_right_2"));
        playerSprites.put(MovableUser.PlayerSprite.LEFT, SpriteInfo.sprites.get("Player_left_1"));
        playerSprites.put(MovableUser.PlayerSprite.LEFT2, SpriteInfo.sprites.get("Player_left_2"));
        playerSprites.put(MovableUser.PlayerSprite.UP, SpriteInfo.sprites.get("Player_up_1"));
        playerSprites.put(MovableUser.PlayerSprite.UP2, SpriteInfo.sprites.get("Player_up_2"));
        playerSprites.put(MovableUser.PlayerSprite.DOWN, SpriteInfo.sprites.get("Player_down_1"));
        playerSprites.put(MovableUser.PlayerSprite.DOWN2, SpriteInfo.sprites.get("Player_down_2"));
        currLevel = new TestLevel(new MovableUser(1, 1, playerSprites), new StaticUser());
        localUser = currLevel.getMovableUser();
        spritesheet = new Spritesheet("/tiles.png");
    }

    public static void main(String[] args) {
        Config.readSprites();
        Engine engine = new Engine();
        engine.createRenderWindow();
        engine.renderStaticTiles();
        engine.renderPlayer(engine.currLevel.getMovableUser(), MovableUser.PlayerSprite.RIGHT);
        engine.renderSpawn();
        engine.renderPlayer(engine.currLevel.getMovableUser(), MovableUser.PlayerSprite.RIGHT);
        engine.renderObjects();
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

    public void renderStaticTiles() {
        BufferedImage image = renderer.renderStatic();
        Image img = image.getScaledInstance(image.getWidth() * Engine.PIXEL_SCALE, image.getHeight() * PIXEL_SCALE, Image.SCALE_DEFAULT);
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

    public void renderObjects() {
        if (objectPanels != null) {
            for (JPanel object : objectPanels) {
                renderer.remove(object);
            }
        }
        objectPanels = renderer.renderObjects();

        for (JPanel object : objectPanels) {
            renderer.add(object, JLayeredPane.MODAL_LAYER);
        }

        renderer.remove(spawnPanel);
        renderer.add(spawnPanel, JLayeredPane.PALETTE_LAYER);

        renderer.remove(playerPanel);
        renderer.add(playerPanel, JLayeredPane.DRAG_LAYER);

        window.revalidate();
    }

    public void renderSpawn() {
        BufferedImage image = renderer.renderSpawn();
        Image img = image.getScaledInstance(image.getWidth() * PIXEL_SCALE, image.getHeight() * PIXEL_SCALE, Image.SCALE_DEFAULT);
        spawnPanel.removeAll();
        spawnPanel.add(new JLabel(new ImageIcon(img)));
        spawnPanel.setBounds(PIXEL_SCALE * image.getHeight() * Board.getSpawnTile(Board.board).getY(), PIXEL_SCALE * image.getHeight() * Board.getSpawnTile(Board.board).getX(), image.getHeight() * PIXEL_SCALE, image.getWidth() * PIXEL_SCALE);

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
                            renderObjects();
                            lastDir = dir;
                            currLevel.incrementCountdown();
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
                            renderObjects();
                            lastDir = dir;
                            currLevel.incrementCountdown();
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
                            renderObjects();
                            lastDir = dir;
                            currLevel.incrementCountdown();
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
                            renderObjects();
                            lastDir = dir;
                            currLevel.incrementCountdown();
                        }
                        break;
                    }
            }
            if(Board.board[user.getY()][user.getX()].getInfo().isEndPoint() && currLevel.isWinnable()) {
                System.out.println("You win!");
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPressed = 0;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(localUser instanceof StaticUser) {
            int x = e.getX()/64;
            int y = e.getY()/64;
            if (Board.board[y][x].getInfo().getDescriptor() == TileInfo.TileDescriptor.CAN_SPAWN) {
                Board.setSpawn(Board.board[y][x]);
                renderSpawn();
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
