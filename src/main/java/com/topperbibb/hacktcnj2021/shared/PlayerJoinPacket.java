package com.topperbibb.hacktcnj2021.shared;

import java.io.ByteArrayOutputStream;

/**
 * Represents a client joining a Room
 * This Packet holds the id of the joining user, and a boolean representing whether they are the host of the Room they have been assigned to
 */
public class PlayerJoinPacket extends Packet {

    public int id;
    public boolean host;

    public PlayerJoinPacket(int id, boolean host) {
        this.id = id;
        this.host = host;
    }

    @Override
    public ByteArrayOutputStream toByteArray() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(3);
        out.write(getTypeInt());
        out.write(id);
        out.write(host ? 1 : 0);
        return out;
    }
}
