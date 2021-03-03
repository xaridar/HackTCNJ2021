package com.topperbibb.hacktcnj2021.shared;

import java.io.ByteArrayOutputStream;

/**
 * Represents a client leaving a Room
 * This Packet holds the id of the leaving user, and a boolean representing whether the receiving client is the room's new host
 */
public class PlayerLeavePacket extends Packet {

    public int id;
    public boolean receiverIsNewHost;

    public PlayerLeavePacket(int id, boolean receiverIsNewHost) {
        this.id = id;
        this.receiverIsNewHost = receiverIsNewHost;
    }

    @Override
    public ByteArrayOutputStream toByteArray() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(3);
        out.write(getTypeInt());
        out.write(id);
        out.write(receiverIsNewHost ? 1 : 0);
        return out;
    }
}
