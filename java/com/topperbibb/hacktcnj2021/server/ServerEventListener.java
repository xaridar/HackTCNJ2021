package com.topperbibb.hacktcnj2021.server;

import com.topperbibb.hacktcnj2021.shared.*;

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
        } else if (p instanceof StateChangePacket) {
            if (!room.hasId(((StateChangePacket) p).id)) {
                return;
            }
            room.sendToAll(p);
        }
    }
}
