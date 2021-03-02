package com.topperbibb.hacktcnj2021.shared;

import java.io.ByteArrayOutputStream;

public class LevelSelectPacket extends Packet {

    public int levelIndex;

    public LevelSelectPacket(int levelIdx) {
        this.levelIndex = levelIdx;
    }

    @Override
    public ByteArrayOutputStream toByteArray() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(2);
        out.write(getTypeInt());
        out.write(levelIndex);
        return out;
    }
}
