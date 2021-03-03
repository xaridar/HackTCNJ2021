package com.topperbibb.hacktcnj2021.shared;

import java.io.ByteArrayOutputStream;

/**
 * Represents a PONG message sent to the server from a client
 * This Packet only holds its type, but no data
 */
public class PongPacket extends Packet {
    @Override
    public ByteArrayOutputStream toByteArray() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(1);
        out.write(getTypeInt());
        return out;
    }
}
