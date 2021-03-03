package com.topperbibb.hacktcnj2021.client;

import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.Engine;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteManager;
import com.topperbibb.hacktcnj2021.client.game.tiles.Tile;
import com.topperbibb.hacktcnj2021.client.game.user.NetUser;
import com.topperbibb.hacktcnj2021.shared.*;

import java.util.List;

/**
 * Manages incoming packets for a {@link Client}
 */
public class ClientEventListener {

    /**
     * Deals with incoming packets based on their type
     *
     * Functions:
     *  - {@link PingPacket}: send a PONG message back
     *  - {@link ConnectPacket}: Set the id and host boolean of the local user
     *  - {@link PlayerJoinPacket}: Add the player locally to the {@link UserManager}
     *  - {@link PlayerLeavePacket}: Remove the p[layer locally, and disconnect the client if the id matches {@link PlayerLeavePacket#id}
     *  - {@link StateChangePacket}: Update the local Board to reflect the {@link StateChangePacket#changes}
     *  - {@link StartPacket}: Starts an {@link Engine} instance locally and starts a game
     *  - {@link EndPacket}: Ends the current game, closes the client, and ends the program
     *  - {@link LevelSelectPacket}: Sets the local {@link Engine#loadIndex} to the {@link LevelSelectPacket#levelIndex} of the Packet
     *
     * @param p to received Packet
     * @param client the Client {@code p} was sent to
     */
    public void received(Packet p, Client client) {
        if (p instanceof PingPacket) {
            client.sendPacket(new PongPacket());
            System.out.println("PONG sent!");
        } else if (p instanceof ConnectPacket) {
            client.user.id = ((ConnectPacket) p).id;
            client.user.host = ((ConnectPacket) p).host;
            System.out.format("Your ID is: %d%s\n", ((ConnectPacket) p).id, ((ConnectPacket) p).host ? " (You are the host)" : "");
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
        } else if (p instanceof StateChangePacket) {
            if (((StateChangePacket) p).id != client.user.id) {
                Board.setSpawnNet(Board.board[((StateChangePacket) p).changes.spawnX][((StateChangePacket) p).changes.spawnY]);
                List<StateChangePacket.Change> changes = ((StateChangePacket) p).changes.changes;
                Tile[][] staticOldBoard = new Tile[Board.board.length][Board.board[0].length];
                for (int x = 0; x < staticOldBoard.length; x++) {
                    for (int y = 0; y < staticOldBoard[x].length; y++) {
                        staticOldBoard[x][y] = Board.board[x][y].copyKeepObj();
                    }
                }
                for (int i = changes.size() - 1; i >= 0; i--) {
                    StateChangePacket.Change change = changes.get(i);
                    Board.moveObj(staticOldBoard[change.oldTileX][change.oldTileY], Board.board[change.newTileX][change.newTileY], SpriteManager.get(change.spriteName));
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
        } else if (p instanceof LevelSelectPacket) {
            System.out.println("Level: " + ((LevelSelectPacket) p).levelIndex + 1);
            Engine.INSTANCE.setLevel(((LevelSelectPacket) p).levelIndex);
        }
    }
}
