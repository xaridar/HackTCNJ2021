package com.topperbibb.hacktcnj2021.client;

import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.shared.*;

public class ClientEventListener {
    public void received(Packet p, Client client) {
        if (p instanceof PingPacket) {
            client.sendPacket(new PongPacket());
            System.out.println("PONG sent!");
        } else if (p instanceof PlayerJoinPacket) {
            if (((PlayerJoinPacket) p).id == client.user.id) {
                UserManager.addUser(client.user);
            } else {
                UserManager.addUser(new User(((PlayerJoinPacket) p).id, ((PlayerJoinPacket) p).host));
            }
            System.out.format("Player %d joined!%s\n", ((PlayerJoinPacket) p).id, ((PlayerJoinPacket) p).host ? " (host)" : "");
        } else if (p instanceof PlayerLeavePacket) {
            User user;
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
            Board.setSpawn(((StateChangePacket) p).changes.newSpawn);
            for (StateChangePacket.Change change : ((StateChangePacket) p).changes.changes) {
                Board.moveObj(change.oldTile, change.newTile);
            }
        }
    }
}
