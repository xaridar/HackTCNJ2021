package com.topperbibb.hacktcnj2021.client;

import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.Engine;
import com.topperbibb.hacktcnj2021.client.game.tiles.Tile;
import com.topperbibb.hacktcnj2021.client.game.user.NetUser;
import com.topperbibb.hacktcnj2021.shared.*;

import java.util.List;

public class ClientEventListener {
    public void received(Packet p, Client client) {
        if (p instanceof PingPacket) {
            client.sendPacket(new PongPacket());
            System.out.println("PONG sent!");
        } else if (p instanceof PlayerJoinPacket) {
            if (((PlayerJoinPacket) p).id == client.user.id) {
                UserManager.addUser(client.user);
            } else {
                UserManager.addUser(new NetUser(((PlayerJoinPacket) p).id, ((PlayerJoinPacket) p).host));
            }
            System.out.format("Player %d joined!%s\n", ((PlayerJoinPacket) p).id, ((PlayerJoinPacket) p).host ? " (host)" : "");
        } else if (p instanceof PlayerLeavePacket) {
            NetUser user;
            if (((PlayerLeavePacket) p).id == client.user.id) {
                user = UserManager.removeUser(client.user.id);
                client.close();
            } else {
                user = UserManager.removeUser(((PlayerLeavePacket) p).id);
            }
            System.out.format("Player %d left!%s\n", ((PlayerLeavePacket) p).id, ((PlayerLeavePacket) p).receiverIsNewHost ? " (You are now the host!)" : "");
        } else if (p instanceof ConnectPacket) {
            client.user.id = ((ConnectPacket) p).id;
            client.user.host = ((ConnectPacket) p).host;
            System.out.format("Your ID is: %d%s\n", ((ConnectPacket) p).id, ((ConnectPacket) p).host ? " (You are the host)" : "");
        } else if (p instanceof StateChangePacket) {
            if (((StateChangePacket) p).id != client.user.id) {
                Board.setSpawnNet(Board.board[((StateChangePacket) p).changes.spawnX][((StateChangePacket) p).changes.spawnY]);
                List<StateChangePacket.Change> changes = ((StateChangePacket) p).changes.changes;
                for (int i = changes.size() - 1; i >= 0; i--) {
                    StateChangePacket.Change change = changes.get(i);
                    Board.moveObj(Board.board[change.oldTileX][change.oldTileY], Board.board[change.newTileX][change.newTileY]);
                }

            }
            Board.lastBoard = new Tile[Board.board.length][Board.board[0].length];
            for (int x = 0; x < Board.board.length; x++) {
                for (int y = 0; y < Board.board[0].length; y++) {
                    Board.lastBoard[x][y] = Board.board[x][y].copyKeepObj();
                }
            }
        } else if (p instanceof StartPacket) {
            System.out.println("The game has started!");
            if (((StartPacket) p).playerType == StartPacket.PlayerType.MOVER) {
                System.out.println("You are the mover.");
                client.user = client.user.asMovable();
            } else {
                System.out.println("You are the overseer.");
                client.user = client.user.asStatic();
            }
            Engine.startEngine(client.user);
        } else if (p instanceof EndPacket) {
            System.out.println("The game has ended!");
            client.close();
            System.exit(0);
        }
    }
}
