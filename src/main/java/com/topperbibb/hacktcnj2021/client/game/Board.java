package com.topperbibb.hacktcnj2021.client.game;

import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteManager;
import com.topperbibb.hacktcnj2021.client.game.objects.BoardObject;
import com.topperbibb.hacktcnj2021.client.game.tiles.Tile;
import com.topperbibb.hacktcnj2021.client.game.tiles.TileInfo;
import com.topperbibb.hacktcnj2021.client.game.user.MovableUser;
import com.topperbibb.hacktcnj2021.shared.StateChangePacket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Manages the current board of the game
 */
public class Board {
    // Holds a copy of the board from the last network update
    public static Tile[][] lastBoard;
    // A 2d array containing the current board
    public static Tile[][] board;
    // The Tile holding the current spawn
    private static Tile spawnTile;
    // The Tile holding the current level's end
    private static Tile endTile;

    /**
     * Loads a board from a {@link String[][]}
     * @param arrayBoard the String array that specifies tile types
     * @param map a Map of Strings to {@link Tile}s specifying meanings of Strings in {@code arrayBoard}
     * @return a new {@link Tile[][]} containing the interpretation of {@code arrayBoard} into Tiles
     */
    public static Tile[][] loadBoard(String[][] arrayBoard, Map<String, Tile> map) {
        Tile[][] tempBoard = new Tile[arrayBoard.length][arrayBoard[0].length];
        for(int x = 0; x < tempBoard.length; x++) {
            for(int y = 0; y < tempBoard[x].length; y++) {
                tempBoard[x][y] = map.getOrDefault(arrayBoard[x][y], new Tile(x, y, new TileInfo(SpriteManager.get("Basic_ground"), TileInfo.TileDescriptor.NO_SPAWN))).copy();
                tempBoard[x][y].setPos(x, y);
                if(tempBoard[x][y].getObject() != null) {
                    tempBoard[x][y].getObject().setPos(x, y);
                }
            }
        }
        return tempBoard;
    }

    /**
     * Returns the Tile on a board that contains a specific BoardObject
     * @param obj the {@link BoardObject} to search for
     * @param board the board to search in
     * @return the associated Tile, or null if the BoardObject does not exist in the board
     */
    public static Tile getTileForObject(BoardObject obj, Tile[][] board) {
        if (obj == null) return null;
        return Arrays.stream(board).flatMap(Arrays::stream).filter(tile -> tile.getObject() == obj).findFirst().orElse(null);
    }

    /**
     * Returns the Tile on a board that contains the spawn
     * @param board the board to search in
     * @return the associated Tile, or null if no spawn tile exists in the board
     */
    public static Tile getSpawnTile(Tile[][] board) {
        return Arrays.stream(board).flatMap(Arrays::stream).filter(tile -> tile.getInfo().isSpawnPoint()).findFirst().orElse(null);
    }

    /**
     * Returns {@link #endTile}
     * @return {@link #endTile}
     */
    public static Tile getSpawnTile() {
        return spawnTile;
    }

    /**
     * Returns the Tile on a board that contains the end
     * @param board the board to search in
     * @return the associated Tile, or null if no end tile exists in the board
     */
    public static Tile getEndTile(Tile[][] board) {
        return Arrays.stream(board).flatMap(Arrays::stream).filter(tile -> tile.getInfo().isEndPoint()).findFirst().orElse(null);
    }

    /**
     * Sets the current spawn to a specific position in the board
     * @param x the x position of the Tile to set as the spawn
     * @param y the y position of the Tile to set as the spawn
     */
    public static void setSpawn(int x, int y) {
        System.out.println(x + ", " + y);
        getSpawnTile(board).getInfo().removeSpawnPoint();
        spawnTile = board[x][y];
        board[x][y].getInfo().setSpawnPoint();
    }

    /**
     * Sets the current spawn, then sends a Packet to the network
     * @param spawn the tile to set as the spawn
     */
    public static void setSpawn(Tile spawn) {
        setSpawnNet(spawn);
        Engine.INSTANCE.applyChanges();
    }

    /**
     * Sets the current spawn without sending a Packet to the network
     * @param spawn the tile to set as the spawn
     */
    public static void setSpawnNet(Tile spawn) {
        getSpawnTile(board).getInfo().removeSpawnPoint();
        spawn.getInfo().setSpawnPoint();
        spawnTile = spawn;
        Engine.INSTANCE.renderSpawn();
    }

    /**
     * Returns {@link #endTile}
     * @return {@link #endTile}
     */
    public static Tile getEndTile() {
        return endTile;
    }

    /**
     * Sets {@link #endTile}
     * @param endTile the new value for {@link #endTile}
     */
    public static void setEndTile(Tile endTile) {
        Board.endTile = endTile;
    }

    /**
     * Moves a {@link BoardObject} from one Tile to another Tile and sets its sprite
     * @param fromTile the Tile containing the BoardObject to move
     * @param toTile the Tile to move the object to
     * @param sprite the {@link SpriteInfo} to set to the object
     */
    public static void moveObj(Tile fromTile, Tile toTile, SpriteInfo sprite) {
        BoardObject obj = fromTile.getObject();
        board[fromTile.getX()][fromTile.getY()].setObject(null);
        toTile.setObject(obj);
        obj.setSprite(sprite);
        if (obj instanceof MovableUser) {
            obj.setPos(toTile.getY(), toTile.getX());
            Engine.INSTANCE.renderPlayer((MovableUser) obj);
        } else {
            obj.setPos(toTile.getX(), toTile.getY());
            Engine.INSTANCE.renderObjects();
        }
    }

    /**
     * Searches for any changes between two boards, and returns them as a {@link com.topperbibb.hacktcnj2021.shared.StateChangePacket.ChangeList}
     * @param oldBoard the old board to compare against
     * @param newBoard the new board to search in
     * @return a ChangeList containing all differences between {@code oldBoard} and {@code newBoard}
     */
    public static StateChangePacket.ChangeList findChanges(Tile[][] oldBoard, Tile[][] newBoard) {
        List<StateChangePacket.Change> changes = new ArrayList<>();
        for (int x = 0; x < oldBoard.length; x++) {
            for (int y = 0; y < oldBoard[x].length; y++) {
                if (oldBoard[x][y].getObject() != newBoard[x][y].getObject()) {
                    if (newBoard[x][y].getObject() != null) {
                        BoardObject obj = newBoard[x][y].getObject();
                        Tile oldTile = getTileForObject(obj, oldBoard);
                        Tile newTile = newBoard[x][y];
                        changes.add(new StateChangePacket.Change(oldTile.getX(), oldTile.getY(), newTile.getX(), newTile.getY(), obj.getSprite()));
                    }
                } else if (newBoard[x][y].getObject() != null && newBoard[x][y].getObject() == oldBoard[x][y].getObject()) {
                    if (newBoard[x][y].getObject().getSprite() != oldBoard[x][y].getObject().getSprite()) {
                        changes.add(new StateChangePacket.Change(x, y, x, y, newBoard[x][y].getObject().getSprite()));
                    }
                }
            }
        }
        return new StateChangePacket.ChangeList(changes, getSpawnTile(newBoard).getX(), getSpawnTile(newBoard).getY());
    }
}
