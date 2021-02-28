package com.topperbibb.hacktcnj2021.shared;

import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.objects.BoardObject;
import com.topperbibb.hacktcnj2021.client.game.tiles.Tile;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class StateChangePacket extends Packet {

    public int id;

    public StateChangePacket(int id) {
        this.id = id;
    }

    @Override
    public ByteArrayOutputStream toByteArray() {
        ByteArrayOutputStream boardState = new ByteArrayOutputStream();
        Tile[][] board = Board.board;
        Tile[][] oldBoard = Board.lastBoard;

        ChangeList changes = Board.findChanges(oldBoard, board);


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        return out;
    }

    public static class Change {
        Tile oldTile;
        Tile newTile;
        BoardObject changed;
        ChangeType type;

        public enum ChangeType {
            MOVE, REMOVE, ADD,
        }

        public Change(ChangeType type, Tile oldTile, Tile newTile, BoardObject changed) {
            this.type = type;
            this.oldTile = oldTile;
            this.newTile = newTile;
            this.changed = changed;
        }
    }

    public static class ChangeList {
        List<Change> changes;
        Tile oldSpawn;
        Tile newSpawn;

        public ChangeList(List<Change> changes, Tile oldSpawn, Tile newSpawn) {
            this.changes = changes;
            this.oldSpawn = oldSpawn;
            this.newSpawn = newSpawn;
        }
    }
}
