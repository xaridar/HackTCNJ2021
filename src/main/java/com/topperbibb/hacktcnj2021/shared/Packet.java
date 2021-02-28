package com.topperbibb.hacktcnj2021.shared;

import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.user.MovableUser;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public abstract class Packet {
    public static Packet from(byte[] in) throws IOException {
        int len = in.length;
        int type = in[0];
        ByteArrayInputStream inputStream =new ByteArrayInputStream(in, 1, len - 1);
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
                changes.add(new StateChangePacket.Change(oldX, oldY, newX, newY));
            }
            return new StateChangePacket(id, new StateChangePacket.ChangeList(changes, spawnX, spawnY));
        }
        return null;
    }

    public abstract ByteArrayOutputStream toByteArray();

    protected int getTypeInt() {
        return PacketType.getTypeIntFromClass(this);
    }

    public enum PacketType {

        CONNECT(0, ConnectPacket.class),
        JOIN(1, PlayerJoinPacket.class),
        LEAVE(2, PlayerLeavePacket.class),
        PING(3, PingPacket.class),
        PONG(4, PongPacket.class),
        START(5, StartPacket.class),
        END(7, EndPacket.class),
        STATE_CHANGE(8, StateChangePacket.class);

        public int i;
        public Class<? extends Packet> cls;
        private PacketType(int i, Class<? extends Packet> cls) {
            this.i = i;
            this.cls = cls;
        }

        public static PacketType typeFromInt(int i) {
            for (PacketType type : values()) {
                if (type.i == i) {
                    return type;
                }
            }
            return null;
        }

        public static Class<? extends Packet> classFromInt(int i) {
            for (PacketType type : values()) {
                if (type.i == i) {
                    return type.cls;
                }
            }
            return null;
        }

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