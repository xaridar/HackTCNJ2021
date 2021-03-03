package com.topperbibb.hacktcnj2021.shared;

import java.io.ByteArrayOutputStream;

/**
 * Represents a game ending for a given room
 * This Packet only holds its type, but no data
 */
public class EndPacket extends Packet {
    @Override
    public ByteArrayOutputStream toByteArray() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(1);
        out.write(getTypeInt());
        return out;
    }
}
