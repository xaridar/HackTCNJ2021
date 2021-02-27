package com.topperbibb.hacktcnj2021.shared;

import java.io.ByteArrayOutputStream;

public class PongPacket extends Packet {
    @Override
    public ByteArrayOutputStream toByteArray() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(1);
        out.write(getTypeInt());
        return out;
    }
}
