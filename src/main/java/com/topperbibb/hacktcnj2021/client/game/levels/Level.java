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
    Tile spawnTile;
    Tile endTile;

    public Level(MovableUser movableUser, StaticUser staticUser){
        this.movableUser = movableUser;
        this.staticUser = staticUser;

        level = Board.loadBoard(setLevel(), mapObjects());

        for (int x = 0; x < level.length; x++) {
            for (int y = 0; y < level[x].length; y++) {
                if(level[x][y].getObject()!=null && level[x][y].getObject() instanceof Key) {
                    keys.add((Key) level[x][y].getObject());
                    System.out.println("Key: "+ level[x][y].getObject().getX() + ", " +level[x][y].getObject().getY());
                }
                if(level[x][y].getInfo().isSpawnPoint()) {
                    spawnTile = level[x][y];
                }else if(level[x][y].getInfo().isEndPoint()) {
                    endTile = level[x][y];
                }
            }
        }

        Board.board = level;
        Board.lastBoard = new Tile[Board.board.length][Board.board[0].length];
        for (int x = 0; x < Board.board.length; x++) {
            for (int y = 0; y < Board.board[0].length; y++) {
                Board.lastBoard[x][y] = Board.board[x][y].copyKeepObj();
            }
        }
        movableUser.setPos(Board.getSpawnTile(Board.board).getY(), Board.getSpawnTile(Board.board).getX());

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
            System.out.println("Countdown finished");
            return false;
        }
        if(keys.size() == 0) {
            System.out.println("No keys");
            return true;
        }
        for (Key key : keys) {
            if(!key.isCollected()) {
                System.out.println("Some keys are uncollected");
                return false;
            }
        }
        System.out.println("All keys collected");
        return true;
    }

    public Tile[][] getLevel() {
        return level;
    }

    public ArrayList<Key> getKeys() {
        return keys;
    }

    public Tile getSpawnTile() {
        return spawnTile;
    }

    public void setSpawnTile(Tile spawnTile) {
        this.spawnTile = spawnTile;
    }

    public Tile getEndTile() {
        return endTile;
    }
}
