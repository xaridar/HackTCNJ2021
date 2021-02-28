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
        Board.lastBoard = level;
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
