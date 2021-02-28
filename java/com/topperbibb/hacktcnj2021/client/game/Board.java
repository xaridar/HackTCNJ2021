package com.topperbibb.hacktcnj2021.client.game;

import com.topperbibb.hacktcnj2021.client.game.objects.BoardObject;
import com.topperbibb.hacktcnj2021.client.game.tiles.Tile;
import com.topperbibb.hacktcnj2021.client.game.tiles.TileInfo;
import com.topperbibb.hacktcnj2021.shared.StateChangePacket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    public static Tile[][] lastBoard;
    public static Tile[][] board;

    public static Tile[][] loadBoard(String[][] arrayBoard) {
        Tile[][] tempBoard = new Tile[arrayBoard.length][arrayBoard[0].length];
        for(int x = 0; x < tempBoard.length; x++) {
            for(int y = 0; y < tempBoard[x].length; y++) {
                tempBoard[x][y] = new Tile(x, y, new TileInfo(arrayBoard[x][y], TileInfo.TileDescriptor.NONE));
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

    public static StateChangePacket.ChangeList findChanges(Tile[][] oldBoard, Tile[][] newBoard) {
        List<StateChangePacket.Change> changes = new ArrayList<>();
        for (int x = 0; x < oldBoard.length; x++) {
            for (int y = 0; y < oldBoard[x].length; y++) {
                if (oldBoard[x][y].getObject() != newBoard[x][y].getObject()) {
                    StateChangePacket.Change.ChangeType type = StateChangePacket.Change.ChangeType.MOVE;
                    BoardObject object = null;
                    Tile oldTile = null;
                    Tile newTile = null;
                    if (newBoard[x][y].getObject() != null) {
                        BoardObject obj = newBoard[x][y].getObject();
                        if (getTileForObject(obj, oldBoard) == null) {
                            type = StateChangePacket.Change.ChangeType.ADD;
                        } else {
                            oldTile = getTileForObject(obj, oldBoard);
                        }
                        newTile = newBoard[x][y];
                        object = obj;
                        changes.add(new StateChangePacket.Change(type, oldTile, newTile, object));
                    } else if (oldBoard[x][y].getObject() != null && getTileForObject(oldBoard[x][y].getObject(), newBoard) == null) {
                        type = StateChangePacket.Change.ChangeType.REMOVE;
                        oldTile = oldBoard[x][y];
                        object = oldTile.getObject();
                        changes.add(new StateChangePacket.Change(type, oldTile, newTile, object));
                    }
                }
            }
        }
        return new StateChangePacket.ChangeList(changes, getSpawnTile(oldBoard), getSpawnTile(newBoard));
    }
}
