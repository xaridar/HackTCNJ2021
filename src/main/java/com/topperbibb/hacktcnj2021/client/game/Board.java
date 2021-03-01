package com.topperbibb.hacktcnj2021.client.game;

import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;
import com.topperbibb.hacktcnj2021.client.game.objects.BoardObject;
import com.topperbibb.hacktcnj2021.client.game.tiles.Tile;
import com.topperbibb.hacktcnj2021.client.game.tiles.TileInfo;
import com.topperbibb.hacktcnj2021.client.game.user.MovableUser;
import com.topperbibb.hacktcnj2021.shared.StateChangePacket;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Map;

public class Board {
    public static Tile[][] lastBoard;
    public static Tile[][] board;
    private static Tile spawnTile;
    private static Tile endTile;

    public static Tile[][] loadBoard(String[][] arrayBoard, Map<String, Tile> map) {
        Tile[][] tempBoard = new Tile[arrayBoard.length][arrayBoard[0].length];
        for(int x = 0; x < tempBoard.length; x++) {
            for(int y = 0; y < tempBoard[x].length; y++) {
                tempBoard[x][y] = map.getOrDefault(arrayBoard[x][y], new Tile(x, y, new TileInfo(new SpriteInfo(16, 0, 0), TileInfo.TileDescriptor.NO_SPAWN))).copy();
                tempBoard[x][y].setPos(x, y);
                if(tempBoard[x][y].getObject() != null) {
                    tempBoard[x][y].getObject().setPos(x, y);
                }
            }
        }
        return tempBoard;
    }

    public static Tile getTileForObject(BoardObject obj, Tile[][] board) {
        if (obj == null) return null;
        return Arrays.stream(board).flatMap(Arrays::stream).filter(tile -> tile.getObject() == obj).findFirst().orElse(null);
    }

    public static Tile getSpawnTile(Tile[][] board) {
        return Arrays.stream(board).flatMap(Arrays::stream).filter(tile -> tile.getInfo().isSpawnPoint()).findFirst().orElse(null);
    }

    public static Tile getSpawnTile() {
        return spawnTile;
    }

    public static Tile getEndTile(Tile[][] board) {
        return Arrays.stream(board).flatMap(Arrays::stream).filter(tile -> tile.getInfo().isEndPoint()).findFirst().orElse(null);
    }

    public static void setSpawn(int x, int y) {
        System.out.println(x + ", " + y);
        getSpawnTile(board).getInfo().removeSpawnPoint();
        board[x][y].getInfo().setSpawnPoint();
    }

    public static void setSpawn(Tile spawn) {
        setSpawnNet(spawn);
        Engine.INSTANCE.applyChanges();
    }

    public static void setSpawnNet(Tile spawn) {
        getSpawnTile(board).getInfo().removeSpawnPoint();
        spawn.getInfo().setSpawnPoint();
        spawnTile = spawn;
        Engine.INSTANCE.renderSpawn();
    }

    public static Tile getEndTile() {
        return endTile;
    }

    public static void setEndTile(Tile endTile) {
        Board.endTile = endTile;
    }

    public static void moveObj(Tile fromTile, Tile toTile) {
        BoardObject obj = fromTile.getObject();
        board[fromTile.getX()][fromTile.getY()].setObject(null);
        toTile.setObject(obj);
        obj.setPos(toTile.getX(), toTile.getY());
        Engine.INSTANCE.renderObjects();
        if (obj instanceof MovableUser) {
            obj.setPos(toTile.getY(), toTile.getX());
            Engine.INSTANCE.renderPlayer(((MovableUser) obj), MovableUser.PlayerSprite.RIGHT);
        }
    }

    public static StateChangePacket.ChangeList findChanges(Tile[][] oldBoard, Tile[][] newBoard) {
        List<StateChangePacket.Change> changes = new ArrayList<>();
        for (int x = 0; x < oldBoard.length; x++) {
            for (int y = 0; y < oldBoard[x].length; y++) {
                if (oldBoard[x][y].getObject() != newBoard[x][y].getObject()) {
                    if (newBoard[x][y].getObject() != null) {
                        BoardObject obj = newBoard[x][y].getObject();
                        Tile oldTile = getTileForObject(obj, oldBoard);
                        Tile newTile = newBoard[x][y];
                        if (obj instanceof MovableUser) {
                            changes.add(new StateChangePacket.Change(oldTile.getX(), oldTile.getY(), newTile.getX(), newTile.getY()));
                        } else {
                            changes.add(new StateChangePacket.Change(oldTile.getX(), oldTile.getY(), newTile.getX(), newTile.getY()));
                        }
                    }
                }
            }
        }
        return new StateChangePacket.ChangeList(changes, getSpawnTile(newBoard).getX(), getSpawnTile(newBoard).getY());
    }
}
