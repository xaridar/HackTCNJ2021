package com.topperbibb.hacktcnj2021.client.game.levels;

import com.topperbibb.hacktcnj2021.client.game.user.MovableUser;
import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.tiles.Tile;
import com.topperbibb.hacktcnj2021.client.game.user.NetUser;
import com.topperbibb.hacktcnj2021.client.game.user.StaticUser;
import com.topperbibb.hacktcnj2021.client.game.util.PlayerKeyEvent;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.Map;

public abstract class Level {
    private Tile[][] level;
    MovableUser movableUser;
    StaticUser staticUser;

    public Level(){
        level = Board.loadBoard(setLevel(), mapObjects());

        Board.board = level;
        Board.lastBoard = new Tile[Board.board.length][Board.board[0].length];
        for (int x = 0; x < Board.board.length; x++) {
            for (int y = 0; y < Board.board[0].length; y++) {
                Board.lastBoard[x][y] = Board.board[x][y].copyKeepObj();
            }
        }
    }

    public abstract String[][] setLevel();

    public abstract Map<String, Tile> mapObjects();

    public void input(PlayerKeyEvent e) {

    }

    public MovableUser getMovableUser() {
        return movableUser;
    }

    public void setMovableUser(MovableUser movableUser) {
        this.movableUser = movableUser;
    }

    public StaticUser getStaticUser() {
        return staticUser;
    }

    public void setStaticUser(StaticUser staticUser) {
        this.staticUser = staticUser;
    }
}
