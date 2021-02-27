package com.topperbibb.hacktcnj2021.shared;

import java.io.ByteArrayOutputStream;

public class ConnectPacket extends Packet {
    public int id;
    public boolean host;

    public ConnectPacket(int id, boolean host) {
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
