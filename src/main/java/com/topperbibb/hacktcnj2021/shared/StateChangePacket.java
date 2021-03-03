package com.topperbibb.hacktcnj2021.shared;

import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;
import com.topperbibb.hacktcnj2021.client.game.objects.BoardObject;
import com.topperbibb.hacktcnj2021.client.game.objects.Player;
import com.topperbibb.hacktcnj2021.client.game.tiles.Tile;
import com.topperbibb.hacktcnj2021.client.game.user.MovableUser;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Represents some sort of Board state change in the game
 * This Packet holds a list of changes made, as well as the id of the sending player
 */
public class StateChangePacket extends Packet {

    public int id;
    public ChangeList changes;

    public StateChangePacket(int id) {
        this.id = id;
    }

    public StateChangePacket(int id, ChangeList changes) {
        this.id = id;
        this.changes = changes;
    }

    @Override
    public ByteArrayOutputStream toByteArray() {
        ByteArrayOutputStream boardState = new ByteArrayOutputStream();
        Tile[][] board = Board.board;
        Tile[][] oldBoard = Board.lastBoard;
        ChangeList changes = this.changes;
        if (changes == null) {
            changes = Board.findChanges(oldBoard, board);
        }
        boardState.write(changes.spawnX);
        boardState.write(changes.spawnY);
        boardState.write(changes.changes.size());
        for (Change ch : changes.changes) {
            boardState.write(ch.oldTileX);
            boardState.write(ch.oldTileY);
            boardState.write(ch.newTileX);
            boardState.write(ch.newTileY);
            boardState.writeBytes(ch.spriteName.getBytes(StandardCharsets.UTF_8));
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(boardState.size() + 2);
        out.write(getTypeInt());
        out.write(id);
        out.writeBytes(boardState.toByteArray());
        return out;
    }

    /**
     * Represents some sort of change in the game board, and includes information about the old and new tile for a moving object, as well as its sprite
     */
    public static class Change {
        public int oldTileX;
        public int oldTileY;
        public int newTileX;
        public int newTileY;
        public String spriteName;

        public Change(int oldTileX, int oldTileY, int newTileX, int newTileY, SpriteInfo sprite) {
            this.oldTileX = oldTileX;
            this.oldTileY = oldTileY;
            this.newTileX = newTileX;
            this.newTileY = newTileY;
            this.spriteName = sprite.key;
        }

        public Change(int oldTileX, int oldTileY, int newTileX, int newTileY, String sprite) {
            this.oldTileX = oldTileX;
            this.oldTileY = oldTileY;
            this.newTileX = newTileX;
            this.newTileY = newTileY;
            this.spriteName = sprite;
        }
    }

    /**
     * Represents an entire list of changes, as well as information about the spawn point at a given state
     */
    public static class ChangeList {
        public List<Change> changes;
        public int spawnX, spawnY;

        public ChangeList(List<Change> changes, int spawnX, int spawnY) {
            this.changes = changes;
            this.spawnX = spawnX;
            this.spawnY = spawnY;
        }
    }
}
