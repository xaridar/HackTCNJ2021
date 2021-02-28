package com.topperbibb.hacktcnj2021.client.game.levels;

import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.tiles.Tile;
import com.topperbibb.hacktcnj2021.client.game.util.PlayerKeyEvent;

import java.awt.*;
import java.util.Map;

public abstract class Level {
    private Tile[][] level;

    public Level(){
        level = Board.loadBoard(setLevel(), mapObjects());

        Board.board = level;
        Board.lastBoard = level;
    }

    public abstract String[][] setLevel();

    public abstract Map<String, Tile> mapObjects();

    public abstract void input(PlayerKeyEvent e);
}
