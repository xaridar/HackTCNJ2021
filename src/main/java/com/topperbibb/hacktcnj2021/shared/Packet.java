package com.topperbibb.hacktcnj2021.shared;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single piece of data passed between the server and client
 */
public abstract class Packet {

    /**
     * Statically parses a byte array into its associated Packet type
     * @param in The byte array to parse
     * @return a new Packet containing the correct type and data based on {@code in}
     */
    public static Packet from(byte[] in)  {
        int len = in.length;
        int type = in[0];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(in, 1, len - 1);
        Class<? extends Packet> packetType = PacketType.classFromInt(type);
        if (packetType == ConnectPacket.class) {
            int id = inputStream.read();
            boolean host = inputStream.read() == 1;
            return new ConnectPacket(id, host);
        } else if (packetType == PlayerJoinPacket.class) {
            int id = inputStream.read();
            boolean host = inputStream.read() == 1;
            return new PlayerJoinPacket(id, host);
        } else if (packetType == PlayerLeavePacket.class) {
            int id = inputStream.read();
            boolean newHost = inputStream.read() == 1;
            return new PlayerLeavePacket(id, newHost);
        } else if (packetType == PingPacket.class) {
            return new PingPacket();
        } else if (packetType == PongPacket.class) {
            return new PongPacket();
        } else if (packetType == StartPacket.class) {
            StartPacket.PlayerType playerType = StartPacket.PlayerType.forInt(inputStream.read());
            return new StartPacket(playerType);
        } else if (packetType == EndPacket.class) {
            return new EndPacket();
        } else if (packetType == StateChangePacket.class) {
            int id = inputStream.read();
            int spawnX = inputStream.read();
            int spawnY = inputStream.read();
            int changeNum = inputStream.read();
            List<StateChangePacket.Change> changes = new ArrayList<>();
            for (int i = 0; i < changeNum; i++) {
                int oldX = inputStream.read();
                int oldY = inputStream.read();
                int newX = inputStream.read();
                int newY = inputStream.read();
                String spriteName = new String(inputStream.readAllBytes());
                changes.add(new StateChangePacket.Change(oldX, oldY, newX, newY, spriteName));
            }
            return new StateChangePacket(id, new StateChangePacket.ChangeList(changes, spawnX, spawnY));
        } else if (packetType == LevelSelectPacket.class) {
            int idx = inputStream.read();
            return new LevelSelectPacket(idx);
        }
        return null;
    }

    /**
     * Abstract method representing how to serialize a Packet
     * @return the Packet serialized as a ByteArrayOutputStream
     */
    public abstract ByteArrayOutputStream toByteArray();

    /**
     * Returns the specific integer descriptor associated with a Packet's type
     * @return the int associated with a Packet type
     */
    protected int getTypeInt() {
        return PacketType.getTypeIntFromClass(this);
    }

    /**
     * Holds information for Packet types
     */
    public enum PacketType {

        CONNECT(0, ConnectPacket.class),
        JOIN(1, PlayerJoinPacket.class),
        LEAVE(2, PlayerLeavePacket.class),
        PING(3, PingPacket.class),
        PONG(4, PongPacket.class),
        START(5, StartPacket.class),
        END(7, EndPacket.class),
        STATE_CHANGE(8, StateChangePacket.class),
        LEVEL_SELECT(9, LevelSelectPacket.class);

        public int i;
        public Class<? extends Packet> cls;
        PacketType(int i, Class<? extends Packet> cls) {
            this.i = i;
            this.cls = cls;
        }

        /**
         * Finds the Packet Class associated with a given int
         * @param i the int to search for in all enum values
         * @return the Packet Class associated with {@code i}, or null if no PacketType includes the given int
         */
        public static Class<? extends Packet> classFromInt(int i) {
            for (PacketType type : values()) {
                if (type.i == i) {
                    return type.cls;
                }
            }
            return null;
        }

        /**
         * Returns {@link #i} for a given Packet's class
         * @param p the Packet to find {@link #i} for
         * @return the type int associated with a given Packet type
         */
        public static int getTypeIntFromClass(Packet p) {
            Class<? extends Packet> cls = p.getClass();
            for (PacketType type : values()) {
                if (type.cls == cls) {
                    return type.i;
                }
            }
            return -1;
        }
    }
}