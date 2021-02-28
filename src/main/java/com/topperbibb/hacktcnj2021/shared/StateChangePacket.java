package com.topperbibb.hacktcnj2021.shared;

import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.objects.BoardObject;
import com.topperbibb.hacktcnj2021.client.game.objects.Player;
import com.topperbibb.hacktcnj2021.client.game.tiles.Tile;
import com.topperbibb.hacktcnj2021.client.game.user.MovableUser;

import java.io.ByteArrayOutputStream;
import java.util.List;

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
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(boardState.size() + 2);
        out.write(getTypeInt());
        out.write(id);
        out.writeBytes(boardState.toByteArray());
        return out;
    }

    public static class Change {
        public int oldTileX;
        public int oldTileY;
        public int newTileX;
        public int newTileY;

        public Change(int oldTileX, int oldTileY, int newTileX, int newTileY) {
            this.oldTileX = oldTileX;
            this.oldTileY = oldTileY;
            this.newTileX = newTileX;
            this.newTileY = newTileY;
        }
    }

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
