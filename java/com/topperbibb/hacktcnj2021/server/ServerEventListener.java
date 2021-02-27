package com.topperbibb.hacktcnj2021.server;

import com.topperbibb.hacktcnj2021.shared.Packet;
import com.topperbibb.hacktcnj2021.shared.PlayerJoinPacket;
import com.topperbibb.hacktcnj2021.shared.PlayerLeavePacket;
import com.topperbibb.hacktcnj2021.shared.PongPacket;

public class ServerEventListener {
    public void received(Packet p, Connection connection, Room room) {
        if (p instanceof PlayerJoinPacket) {
            room.sendToAll(p);
        } else if (p instanceof PlayerLeavePacket) {
            Connection leaving = RoomManager.getConnById(room, ((PlayerLeavePacket) p).id);
            if (leaving == null) {
                return;
            }
            room.sendPlayerLeave(leaving);
        } else if (p instanceof PongPacket) {
            if (!connection.lookingForPong) return;
            connection.resetPing();
        }
    }
}
