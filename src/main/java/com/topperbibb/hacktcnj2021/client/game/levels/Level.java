package com.topperbibb.hacktcnj2021.client.game.levels;

import com.topperbibb.hacktcnj2021.client.User;
import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.tiles.Tile;
import com.topperbibb.hacktcnj2021.client.game.util.PlayerKeyEvent;

import java.awt.*;
import java.util.Map;

public abstract class Level {
    private Tile[][] level;
    User player;

    public Level(){
        level = Board.loadBoard(setLevel(), mapObjects());

        Board.board = level;
        Board.lastBoard = level;
    }

    public abstract String[][] setLevel();

    public abstract Map<String, Tile> mapObjects();

    public void input(PlayerKeyEvent e) {

    }

    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
    }
}
