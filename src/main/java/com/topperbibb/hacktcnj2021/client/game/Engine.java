package com.topperbibb.hacktcnj2021.client.game;

import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteManager;
import com.topperbibb.hacktcnj2021.client.game.levels.*;
import com.topperbibb.hacktcnj2021.client.UserManager;
import com.topperbibb.hacktcnj2021.client.game.tiles.Tile;
import com.topperbibb.hacktcnj2021.client.game.tiles.TileInfo;
import com.topperbibb.hacktcnj2021.client.game.user.MovableUser;
import com.topperbibb.hacktcnj2021.client.game.graphics.LevelRenderer;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;
import com.topperbibb.hacktcnj2021.client.game.user.NetUser;
import com.topperbibb.hacktcnj2021.client.game.user.StaticUser;
import com.topperbibb.hacktcnj2021.shared.LevelSelectPacket;
import com.topperbibb.hacktcnj2021.shared.StateChangePacket;

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
import java.util.stream.Collectors;

public class Engine implements KeyListener, MouseListener {

    private final NetUser localUser;

    public static Level[] loadOrder;

    public static int loadIndex = 0;
    private boolean singlePlayer = false;
    private Level currLevel;
    private LevelRenderer renderer;
    private JFrame window;
    private JPanel levelPanel;
    private JPanel playerPanel;
    private JPanel spawnPanel;
    private JPanel endPanel;
    private ArrayList<JPanel> objectPanels;
    public static final long keyTimeout = 300;
    private long keyPressed;


    public static Engine INSTANCE;

    public Engine(NetUser p) {
        Map<MovableUser.PlayerSprite, String> playerSprites = new HashMap<>();
        playerSprites.put(MovableUser.PlayerSprite.RIGHT, "Player_right");
        playerSprites.put(MovableUser.PlayerSprite.LEFT, "Player_left");
        playerSprites.put(MovableUser.PlayerSprite.UP, "Player_up");
        playerSprites.put(MovableUser.PlayerSprite.DOWN, "Player_down");
        NetUser other = UserManager.users.stream().filter(user -> user != p).collect(Collectors.toList()).get(0);
        MovableUser movable;
        StaticUser staticUser;
        if (p instanceof MovableUser) {
            movable = (MovableUser) p;
            staticUser = other.asStatic();
        } else {
            movable = other.asMovable();
            staticUser = (StaticUser) p;
        }
        movable.setSprites(playerSprites);
        loadOrder = new Level[]{
                new Tutorial(movable, staticUser),
                new Wall(movable, staticUser),
                new KeyShowcase(movable, staticUser),
                new TestLevel(movable, staticUser)
        };
        currLevel = loadOrder[loadIndex];
        Board.setSpawn(currLevel.startSpawnX, currLevel.startSpawnY);
        Board.board = currLevel.getLevel();
        Board.setEndTile(currLevel.getEndTile());
        movable.setPos(Board.getSpawnTile(Board.board).getX(), Board.getSpawnTile(Board.board).getY());
        Board.getSpawnTile(Board.board).setObject(movable);
        localUser = p;
        movable.setSprite(movable.getSprite(MovableUser.PlayerSprite.RIGHT));
        Board.lastBoard = new Tile[Board.board.length][Board.board[0].length];
        for (int x = 0; x < Board.board.length; x++) {
            for (int y = 0; y < Board.board[0].length; y++) {
                Board.lastBoard[x][y] = Board.board[x][y].copyKeepObj();
            }
        }
    }

    public Engine(MovableUser u, boolean b) {
        singlePlayer = b;
        Map<MovableUser.PlayerSprite, String> playerSprites = new HashMap<>();
        playerSprites.put(MovableUser.PlayerSprite.RIGHT, "Player_right");
        playerSprites.put(MovableUser.PlayerSprite.LEFT, "Player_left");
        playerSprites.put(MovableUser.PlayerSprite.UP, "Player_up");
        playerSprites.put(MovableUser.PlayerSprite.DOWN, "Player_down");
        u.setSprites(playerSprites);
        StaticUser staticUser = new StaticUser();
        loadOrder = new Level[]{
                new Tutorial(u, staticUser),
                new Wall(u, staticUser),
                new KeyShowcase(u, staticUser),
                new TestLevel(u, staticUser)
        };
        currLevel = loadOrder[3];
        Board.setSpawn(currLevel.startSpawnX, currLevel.startSpawnY);
        Board.board = currLevel.getLevel();
        Board.setEndTile(currLevel.getEndTile());
        u.setPos(Board.getSpawnTile(Board.board).getX(), Board.getSpawnTile(Board.board).getY());
        Board.getSpawnTile(Board.board).setObject(u);
        localUser = u;
        u.setSprite(u.getSprite(MovableUser.PlayerSprite.RIGHT));
        Board.lastBoard = new Tile[Board.board.length][Board.board[0].length];
        for (int x = 0; x < Board.board.length; x++) {
            for (int y = 0; y < Board.board[0].length; y++) {
                Board.lastBoard[x][y] = Board.board[x][y].copyKeepObj();
            }
        }
    }

    public static void startEngine(NetUser self) {
        Config.readSprites();
        Engine engine = new Engine(self);
        INSTANCE = engine;
        engine.createRenderWindow();
        engine.renderStaticTiles();
        engine.renderPlayer(engine.currLevel.getMovableUser());
        engine.renderSpawn();
        engine.renderEnd();
        engine.renderObjects();
    }

    public static void startSingleplayerEngine(MovableUser u) {
        Config.readSprites();
        Engine engine = new Engine(u, true);
        INSTANCE = engine;
        engine.createRenderWindow();
        engine.renderStaticTiles();
        engine.renderSpawn();
        engine.renderEnd();
        engine.renderPlayer(engine.currLevel.getMovableUser());
        engine.renderObjects();
    }

    public void createRenderWindow() {
        window = new JFrame();
        window.setPreferredSize(new Dimension((int) (Board.board[0].length * SpriteManager.tileSize * SpriteManager.pixelScale) + 16, (int) (Board.board.length * SpriteManager.tileSize * SpriteManager.pixelScale)+ 38));
        window.setLayout(new BorderLayout());
        renderer = new LevelRenderer();
        window.add(renderer, BorderLayout.CENTER);
        renderer.setBounds(0, 0, (int) (Board.board[0].length * SpriteManager.tileSize * SpriteManager.pixelScale), (int) (Board.board.length * SpriteManager.tileSize * SpriteManager.pixelScale));

        levelPanel = new JPanel(new BorderLayout());
        levelPanel.setBackground(new Color(0, 0, 0, 0));
        levelPanel.setBounds(0, 0, (int) (Board.board[0].length * SpriteManager.tileSize * SpriteManager.pixelScale), (int) (Board.board.length * SpriteManager.tileSize * SpriteManager.pixelScale));
        renderer.add(levelPanel, JLayeredPane.DEFAULT_LAYER);

        playerPanel = new JPanel(new BorderLayout());
        playerPanel.setBackground(new Color(0, 0, 0, 0));
        playerPanel.setBounds(0, 0, SpriteManager.tileSize, SpriteManager.tileSize);
        renderer.add(playerPanel, JLayeredPane.MODAL_LAYER);

        spawnPanel = new JPanel(new BorderLayout());
        spawnPanel.setBackground(new Color(0, 0, 0, 0));
        spawnPanel.setBounds((int) (SpriteManager.tileSize * SpriteManager.pixelScale * Board.getSpawnTile(Board.board).getX()), (int) (SpriteManager.tileSize * SpriteManager.pixelScale * Board.getSpawnTile(Board.board).getY()), SpriteManager.tileSize, SpriteManager.tileSize);
        renderer.add(spawnPanel, JLayeredPane.PALETTE_LAYER);

        endPanel = new JPanel(new BorderLayout());
        endPanel.setBackground(new Color(0, 0, 0, 0));
        endPanel.setBounds((int) (SpriteManager.tileSize * SpriteManager.pixelScale * Board.getEndTile(Board.board).getX()), (int) (SpriteManager.tileSize * SpriteManager.pixelScale * Board.getEndTile(Board.board).getY()), SpriteManager.tileSize, SpriteManager.tileSize);
        renderer.add(endPanel, JLayeredPane.PALETTE_LAYER);

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
        Image img = image.getScaledInstance((int) (image.getWidth() * SpriteManager.pixelScale), (int) (image.getHeight() * SpriteManager.pixelScale), Image.SCALE_DEFAULT);
        levelPanel.removeAll();
        levelPanel.add(new JLabel(new ImageIcon(img)));
        renderer.remove(levelPanel);
        renderer.add(levelPanel, JLayeredPane.DEFAULT_LAYER);
        window.revalidate();
        window.pack();
    }

    public void renderPlayer(MovableUser player) {
        Image img = renderer.renderPlayer(player);
        img = img.getScaledInstance(
                Math.min((int) (SpriteManager.tileSize * SpriteManager.pixelScale),(int) (player.getSprite().width * player.getSprite().pixelScale)),
                Math.min((int) (SpriteManager.tileSize * SpriteManager.pixelScale),(int) (player.getSprite().height * player.getSprite().pixelScale)),
                Image.SCALE_DEFAULT
        );
        playerPanel.removeAll();
        ImageIcon ii = new ImageIcon(img);
        JLabel image = new JLabel(ii);
        System.out.println(ii.getIconWidth());
        image.setBounds(
                (int) ((SpriteManager.tileSize * SpriteManager.pixelScale - player.getSprite().width * player.getSprite().pixelScale) / 2),
                (int) ((SpriteManager.tileSize * SpriteManager.pixelScale - player.getSprite().height * player.getSprite().pixelScale) / 2),
                player.getSprite().width,
                player.getSprite().height
        );
        playerPanel.add(image);
        playerPanel.setBounds((int) (SpriteManager.pixelScale * SpriteManager.tileSize * player.x), (int) (SpriteManager.pixelScale * SpriteManager.tileSize * player.y), (int) (SpriteManager.tileSize * SpriteManager.pixelScale), (int) (SpriteManager.tileSize * SpriteManager.pixelScale));
        window.revalidate();
        window.pack();
    }

    public void renderObjects() {
        if (objectPanels != null) {
            for (JPanel object : objectPanels) {
                renderer.remove(object);
                object.invalidate();
            }
        }
        objectPanels = renderer.renderObjects();

        for (JPanel object : objectPanels) {
            renderer.add(object, JLayeredPane.MODAL_LAYER);
            object.setBackground(new Color(0, 0, 0, 0));
        }

        renderer.remove(spawnPanel);
        renderer.add(spawnPanel, JLayeredPane.PALETTE_LAYER);
        renderer.remove(endPanel);
        renderer.add(endPanel, JLayeredPane.PALETTE_LAYER);

        renderer.remove(playerPanel);
        renderer.add(playerPanel, JLayeredPane.DRAG_LAYER);

        window.revalidate();
    }

    public void renderSpawn() {
        SpriteInfo sprite = SpriteManager.get("Spawn_point");
        BufferedImage image = renderer.renderSpawn();
        Image img = image.getScaledInstance((int) (sprite.width * sprite.pixelScale), (int) (sprite.height * sprite.pixelScale), Image.SCALE_DEFAULT);
        spawnPanel.removeAll();
        JLabel label = new JLabel(new ImageIcon(img));
        label.setBounds((int) ((SpriteManager.tileSize * SpriteManager.pixelScale - sprite.width * sprite.pixelScale) / 2), (int) ((SpriteManager.tileSize * SpriteManager.pixelScale - sprite.height * sprite.pixelScale) / 2), (int) (sprite.width * sprite.pixelScale), (int) (sprite.height * sprite.pixelScale));
        spawnPanel.add(label);
        spawnPanel.setBounds((int) (SpriteManager.pixelScale * SpriteManager.tileSize * Board.getSpawnTile(Board.board).getY()), (int) (SpriteManager.pixelScale * SpriteManager.tileSize * Board.getSpawnTile(Board.board).getX()), (int) (image.getHeight() * SpriteManager.pixelScale), (int) (image.getWidth() * SpriteManager.pixelScale));

        window.revalidate();
        window.pack();
    }

    public void renderEnd() {
        SpriteInfo sprite = SpriteManager.get("End");
        BufferedImage image = renderer.renderEnd();
        Image img = image.getScaledInstance((int) (sprite.width * sprite.pixelScale), (int) (sprite.height * sprite.pixelScale), Image.SCALE_DEFAULT);
        endPanel.removeAll();
        JLabel label = new JLabel(new ImageIcon(img));
        label.setBounds((int) ((SpriteManager.tileSize * SpriteManager.pixelScale - sprite.width * sprite.pixelScale) / 2), (int) ((SpriteManager.tileSize * SpriteManager.pixelScale - sprite.height * sprite.pixelScale) / 2), (int) (sprite.width * sprite.pixelScale), (int) (sprite.height * sprite.pixelScale));
        endPanel.add(label);
        endPanel.setBounds((int) (SpriteManager.pixelScale * SpriteManager.tileSize * Board.getEndTile(Board.board).getY()), (int) (SpriteManager.pixelScale * SpriteManager.tileSize * Board.getEndTile(Board.board).getX()), (int) (image.getHeight() * SpriteManager.pixelScale), (int) (image.getWidth() * SpriteManager.pixelScale));

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
            MovableUser user = currLevel.getMovableUser();
            switch (e.getKeyChar()) {
                case 'W':
                case 'w':
                    if (keyPressed + keyTimeout <= System.currentTimeMillis()) {
                        if (user.move(0, -1)) {
                            currLevel.incrementCountdown();
                        }
                        renderObjects();
                        renderPlayer(user);
                        keyPressed = System.currentTimeMillis();
                    }
                    break;
                case 'S':
                case 's':
                    if (keyPressed + keyTimeout <= System.currentTimeMillis()) {
                        if (user.move(0, 1)) {
                            currLevel.incrementCountdown();
                        }
                        renderObjects();
                        renderPlayer(user);
                        keyPressed = System.currentTimeMillis();
                    }
                    break;
                case 'A':
                case 'a':
                    if (keyPressed + keyTimeout <= System.currentTimeMillis()) {
                        if (user.move(-1, 0)) {
                            currLevel.incrementCountdown();
                        }
                        renderObjects();
                        renderPlayer(user);
                        keyPressed = System.currentTimeMillis();
                    }
                    break;
                case 'D':
                case 'd':
                    if (keyPressed + keyTimeout <= System.currentTimeMillis()) {
                        if (user.move(1, 0)) {
                            currLevel.incrementCountdown();
                        }
                        renderObjects();
                        renderPlayer(user);
                        keyPressed = System.currentTimeMillis();
                    }
                    break;
                case 'Q':
                case 'q':
                    user.die();
                    renderPlayer(user);
                    renderObjects();
                    keyPressed = System.currentTimeMillis();
                    break;
            }
        }
        if(Board.board[currLevel.getMovableUser().getY()][currLevel.getMovableUser().getX()].getInfo().isEndPoint() && currLevel.isWinnable()) {
            if (localUser.host) {
                localUser.sendPacket(new LevelSelectPacket(loadIndex + 1));
            } else if (singlePlayer) {
                setLevel(loadIndex + 1);
            }
        }
        if(e.getKeyChar() == 'r') {
            currLevel = loadOrder[loadIndex];
            currLevel.reset();

            renderStaticTiles();
            renderSpawn();
            renderEnd();
            renderPlayer(currLevel.getMovableUser());
            renderObjects();
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
        if(localUser instanceof StaticUser || singlePlayer) {
            int x = e.getX()/(int) (SpriteManager.pixelScale * SpriteManager.tileSize);
            int y = e.getY()/(int) (SpriteManager.pixelScale * SpriteManager.tileSize);
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

    public void applyChanges() {
        if (!singlePlayer)
            localUser.sendPacket(new StateChangePacket(localUser.id));
    }

    public void setLevel(int levelIndex) {
        loadIndex = levelIndex;
        currLevel = loadOrder[loadIndex];

        Board.board = currLevel.getLevel();
        Board.lastBoard = currLevel.getLevel();
        Board.setSpawn(currLevel.getSpawnTile());
        Board.setEndTile(currLevel.getEndTile());
        currLevel.getMovableUser().setPos(Board.getSpawnTile().getY(), Board.getSpawnTile().getX());
        renderStaticTiles();
        currLevel.getMovableUser().setSprite(currLevel.getMovableUser().getSprite(MovableUser.PlayerSprite.RIGHT));
        renderPlayer(currLevel.getMovableUser());
        renderObjects();
        renderSpawn();
        renderEnd();
        System.out.println(Board.getSpawnTile().getY() + ", " + Board.getSpawnTile().getX());
        System.out.println(currLevel.getMovableUser().getY() + ", " + currLevel.getMovableUser().getX());
        System.out.println("You win!");
    }
}
