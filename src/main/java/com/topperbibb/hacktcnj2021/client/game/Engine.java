package com.topperbibb.hacktcnj2021.client.game;

import com.topperbibb.hacktcnj2021.client.UserManager;
import com.topperbibb.hacktcnj2021.client.game.graphics.LevelRenderer;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteManager;
import com.topperbibb.hacktcnj2021.client.game.levels.*;
import com.topperbibb.hacktcnj2021.client.game.tiles.Tile;
import com.topperbibb.hacktcnj2021.client.game.user.MovableUser;
import com.topperbibb.hacktcnj2021.client.game.user.NetUser;
import com.topperbibb.hacktcnj2021.client.game.user.StaticUser;
import com.topperbibb.hacktcnj2021.client.game.util.PlayerKeyListener;
import com.topperbibb.hacktcnj2021.client.game.util.PlayerMouseListener;
import com.topperbibb.hacktcnj2021.shared.StateChangePacket;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Runs a game and manages all game actions and variables
 */
public class Engine {

    // The local user of this client
    public final NetUser localUser;

    // A list of Levels in order for level progression
    public static Level[] loadOrder;

    // The index of the current Level in loadOrder
    public static int loadIndex = 0;
    // A boolean representing whether the game is being played locally
    public boolean singlePlayer = false;
    // The current Level
    private Level currLevel;
    // The LevelRenderer, which displays the GUI for the game
    private LevelRenderer renderer;

    // Main components for the Swing GUI
    private JFrame window;
    private JPanel levelPanel;
    private JPanel playerPanel;
    private JPanel spawnPanel;
    private JPanel endPanel;
    private ArrayList<JPanel> objectPanels;
    public static final long keyTimeout = 300;
    private long keyPressed;


    // Holds the current Engine (for singleton use)
    public static Engine INSTANCE;

    /**
     * Creates a multiplayer Engine from the local player
     * @param p the local NetUser, set before this instance is created
     */
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

    /**
     * Creates a single player Engine from the local player
     * @param u a MovableUser instance to be used as the player
     * @param b a boolean representing whether this Engine should be single player (always called with true)
     */
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
        currLevel = loadOrder[loadIndex];
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

    /**
     * Starts a new multiplayer Engine instance and starts the game
     * @param self the local user of the game
     */
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

    /**
     * Creates a new single player Engine instance and starts a game
     * @param u a MovableUser to use as the player
     */
    public static void startSinglePlayerEngine(MovableUser u) {
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

    /**
     * Instantiates {@link #window} and all of its panels for GUI
     */
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

        renderer.addKeyListener(new PlayerKeyListener(this));
        renderer.addMouseListener(new PlayerMouseListener(this));
        renderer.setFocusable(true);
        renderer.requestFocusInWindow();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
    }

    /**
     * Renders static tiles, such as ground and walls
     */
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

    /**
     * Renders a player on the window
     * @param player the MovableUser to render
     */
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

    /**
     * Render all non-player objects on the board
     */
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

    /**
     * Renders the spawn sprite on top of the appropriate tile
     */
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

    /**
     * Renders the end sprite on top of the appropriate tile
     */
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

    /**
     * Returns {@link #currLevel}
     * @return {@link #currLevel}
     */
    public Level getCurrLevel() {
        return currLevel;
    }

    /**
     * Signifies some sort of change in the game, which sends a {@link StateChangePacket} to the server if the game is online
     */
    public void applyChanges() {
        if (!singlePlayer)
            localUser.sendPacket(new StateChangePacket(localUser.id));
    }

    /**
     * Changes the current level based on an index
     * @param levelIndex the index to set the level to
     */
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
        System.out.println("You win!");
    }

    public void setCurrLevel(Level level) {
        this.currLevel = level;
    }
}
