package com.topperbibb.hacktcnj2021.client.game.util;

import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.Engine;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteManager;
import com.topperbibb.hacktcnj2021.client.game.tiles.TileInfo;
import com.topperbibb.hacktcnj2021.client.game.user.StaticUser;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Handles Mouse events from the player
 */
public class PlayerMouseListener extends MouseAdapter {

    private final Engine engine;

    public PlayerMouseListener(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(engine.localUser instanceof StaticUser || engine.singlePlayer) {
            int x = e.getX()/(int) (SpriteManager.pixelScale * SpriteManager.tileSize);
            int y = e.getY()/(int) (SpriteManager.pixelScale * SpriteManager.tileSize);
            if (Board.board[y][x].getInfo().getDescriptor() == TileInfo.TileDescriptor.CAN_SPAWN) {
                Board.setSpawn(Board.board[y][x]);
                engine.renderSpawn();
            }
        }
    }

}
