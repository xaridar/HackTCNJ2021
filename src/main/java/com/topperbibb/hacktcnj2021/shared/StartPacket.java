package com.topperbibb.hacktcnj2021.shared;

import java.io.ByteArrayOutputStream;

/**
 * Represents a game starting for a given room
 * This Packet is different for every player it is sent to: it includes the player's game role
 */
public class StartPacket extends Packet {

    public enum PlayerType {
        OVERSEER(0),
        MOVER(1);

        int i;
        PlayerType(int i) { this.i = i; }

        public static PlayerType forInt(int i) {
            return i == 1 ? MOVER : OVERSEER;
        }
    }

    public PlayerType playerType;

    public StartPacket(PlayerType  p) {
        playerType = p;
    }

    @Override
    public ByteArrayOutputStream toByteArray() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(2);
        out.write(getTypeInt());
        out.write(playerType.i);
        return out;
    }
}
