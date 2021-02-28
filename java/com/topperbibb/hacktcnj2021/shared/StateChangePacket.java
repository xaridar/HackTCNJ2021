package com.topperbibb.hacktcnj2021.shared;

import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.objects.BoardObject;
import com.topperbibb.hacktcnj2021.client.game.tiles.Tile;

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
        boardState.write(changes.newSpawn.getX());
        boardState.write(changes.newSpawn.getY());
        boardState.write(changes.changes.size());
        for (Change ch : changes.changes) {
            boardState.write(ch.oldTile.getX());
            boardState.write(ch.oldTile.getY());
            boardState.write(ch.newTile.getX());
            boardState.write(ch.newTile.getY());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(boardState.size() + 2);
        out.write(getTypeInt());
        out.write(id);
        out.writeBytes(boardState.toByteArray());
        return out;
    }

    public static class Change {
        public Tile oldTile;
        public Tile newTile;

        public Change(Tile oldTile, Tile newTile) {
            this.newTile = newTile;
        }
    }

    public static class ChangeList {
        public List<Change> changes;
        public Tile newSpawn;

        public ChangeList(List<Change> changes, Tile newSpawn) {
            this.changes = changes;
            this.newSpawn = newSpawn;
        }
    }
}
