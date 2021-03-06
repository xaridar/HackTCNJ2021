package com.topperbibb.hacktcnj2021.client.game.util;

import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.Engine;
import com.topperbibb.hacktcnj2021.client.game.user.MovableUser;
import com.topperbibb.hacktcnj2021.shared.LevelSelectPacket;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static java.awt.event.KeyEvent.*;

/**
 * Handles Key events from the player
 */
public class PlayerKeyListener extends KeyAdapter {

    private final Engine engine;

    // Longs used for key input spacing
    public static final long keyTimeout = 300;
    private long keyPressed;

    public PlayerKeyListener(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (engine.localUser instanceof MovableUser) {
            MovableUser user = engine.getCurrLevel().getMovableUser();
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                case VK_UP:
                    if (keyPressed + keyTimeout <= System.currentTimeMillis()) {
                        if (user.move(0, -1)) {
                            engine.getCurrLevel().incrementCountdown();
                        }
                        engine.renderObjects();
                        engine.renderPlayer(user);
                        keyPressed = System.currentTimeMillis();
                    }
                    break;
                case KeyEvent.VK_S:
                case VK_DOWN:
                    if (keyPressed + keyTimeout <= System.currentTimeMillis()) {
                        if (user.move(0, 1)) {
                            engine.getCurrLevel().incrementCountdown();
                        }
                        engine.renderObjects();
                        engine.renderPlayer(user);
                        keyPressed = System.currentTimeMillis();
                    }
                    break;
                case KeyEvent.VK_A:
                case VK_LEFT:
                    if (keyPressed + keyTimeout <= System.currentTimeMillis()) {
                        if (user.move(-1, 0)) {
                            engine.getCurrLevel().incrementCountdown();
                        }
                        engine.renderObjects();
                        engine.renderPlayer(user);
                        keyPressed = System.currentTimeMillis();
                    }
                    break;
                case KeyEvent.VK_D:
                case VK_RIGHT:
                    if (keyPressed + keyTimeout <= System.currentTimeMillis()) {
                        if (user.move(1, 0)) {
                            engine.getCurrLevel().incrementCountdown();
                        }
                        engine.renderObjects();
                        engine.renderPlayer(user);
                        keyPressed = System.currentTimeMillis();
                    }
                    break;
                case KeyEvent.VK_Q:
                    user.die();
                    engine.renderPlayer(user);
                    engine.renderObjects();
                    keyPressed = System.currentTimeMillis();
                    break;
            }
        }
        if(Board.board[engine.getCurrLevel().getMovableUser().getY()][engine.getCurrLevel().getMovableUser().getX()].getInfo().isEndPoint() && engine.getCurrLevel().isWinnable()) {
            if (engine.localUser.host) {
                engine.localUser.sendPacket(new LevelSelectPacket(Engine.loadIndex + 1));
            } else if (engine.singlePlayer) {
                engine.setLevel(Engine.loadIndex + 1);
            }
        }
        if(e.getKeyChar() == 'r') {
            engine.setCurrLevel(Engine.loadOrder[Engine.loadIndex]);
            engine.getCurrLevel().reset();

            engine.renderStaticTiles();
            engine.renderSpawn();
            engine.renderEnd();
            engine.renderPlayer(engine.getCurrLevel().getMovableUser());
            engine.renderObjects();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPressed = 0;
    }

}
