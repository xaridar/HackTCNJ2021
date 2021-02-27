package com.topperbibb.hacktcnj2021.shared;

import java.io.ByteArrayOutputStream;

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
