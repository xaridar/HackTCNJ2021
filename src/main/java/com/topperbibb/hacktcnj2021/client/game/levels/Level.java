package com.topperbibb.hacktcnj2021.client.game.levels;

import com.topperbibb.hacktcnj2021.client.game.objects.Key;
import com.topperbibb.hacktcnj2021.client.game.user.MovableUser;
import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.tiles.Tile;
import com.topperbibb.hacktcnj2021.client.game.user.NetUser;
import com.topperbibb.hacktcnj2021.client.game.user.StaticUser;
import com.topperbibb.hacktcnj2021.client.game.util.PlayerKeyEvent;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.ArrayList;
import java.util.Map;

public abstract class Level {
    private Tile[][] level;
    MovableUser movableUser;
    StaticUser staticUser;
    ArrayList<Key> keys = new ArrayList<>();
    int levelCountdown = -1;
    public int startSpawnX;
    public int startSpawnY;

    public Level(){
        level = Board.loadBoard(setLevel(), mapObjects());

        for (int x = 0; x < level.length; x++) {
            for (int y = 0; y < level[x].length; y++) {
                if(level[x][y].getObject()!=null && level[x][y].getObject() instanceof Key) {
                    keys.add((Key) level[x][y].getObject());
                    System.out.println(((Key) level[x][y].getObject()).getX() + ", " +((Key) level[x][y].getObject()).getY());
                }
            }
        }

        Board.board = level;
        startSpawnX = Board.getSpawnTile(Board.board).getX();
        startSpawnY = Board.getSpawnTile(Board.board).getY();
        Board.lastBoard = new Tile[Board.board.length][Board.board[0].length];
        for (int x = 0; x < Board.board.length; x++) {
            for (int y = 0; y < Board.board[0].length; y++) {
                Board.lastBoard[x][y] = Board.board[x][y].copyKeepObj();
            }
        }
    }

    public abstract String[][] setLevel();

    public abstract Map<String, Tile> mapObjects();

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

    protected int setLevelCountdown() {return -1;}

    public void incrementCountdown() {
        levelCountdown = levelCountdown <= 0 ? levelCountdown : levelCountdown-1;
    }

    public boolean isWinnable() {
        if(levelCountdown == 0) {
            return false;
        }
        if(keys.size() == 0) {
            return true;
        }
        for (Key key : keys) {
            if(!key.isCollected()) {
                return false;
            }
        }
        return true;
    }
}
