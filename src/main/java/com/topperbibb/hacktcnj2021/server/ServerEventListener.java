package com.topperbibb.hacktcnj2021.server;

import com.topperbibb.hacktcnj2021.shared.*;

/**
 * Manages incoming {@link Packet}s for Connections by routing them correctly
 */
public class ServerEventListener {

    /**
     * Accepts and deals with a new Packet
     * @param p a received Packet
     * @param connection the connection that received said Packet
     * @param room the room of the received Packet
     */
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
        } else if (p instanceof LevelSelectPacket) {
            if (!connection.host) return;
            room.sendToAll(p);
        }
    }
}
