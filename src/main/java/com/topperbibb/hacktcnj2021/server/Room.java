package com.topperbibb.hacktcnj2021.server;

import com.topperbibb.hacktcnj2021.shared.Packet;
import com.topperbibb.hacktcnj2021.shared.PlayerLeavePacket;
import com.topperbibb.hacktcnj2021.shared.StartPacket;
import com.topperbibb.hacktcnj2021.shared.EndPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Room {

    public boolean playing = false;
    public boolean hasOverseer = false;

    public void sendToAll(Packet p) {
        RoomManager.getAllConnections(this).forEach(conn -> conn.sendPacket(p));
    }

    public void checkForStart() {
        if (RoomManager.canPlay(this)) {
            sendStart();
            playing = true;
        } else {
            if (playing) {
                sendToAll(new EndPacket());
            }
        }
    }

    private void sendStart() {
        Random random = new Random();
        StartPacket.PlayerType playerType = random.nextInt() == 1 ? StartPacket.PlayerType.OVERSEER : StartPacket.PlayerType.MOVER;
        List<Connection> connectionSet = RoomManager.getAllConnections(this);
        connectionSet.get(0).sendPacket(new StartPacket(playerType));
        connectionSet.get(1).sendPacket(new StartPacket(playerType == StartPacket.PlayerType.OVERSEER ? StartPacket.PlayerType.MOVER : StartPacket.PlayerType.OVERSEER));
    }

    public void sendPlayerLeave(Connection connection) {
        List<Connection> conns = RoomManager.getAllConnections(this);
        final Connection[] host = {conns.stream().filter(conn -> conn.host).collect(Collectors.toList()).get(0)};
        if (connection.host) {
            List<Connection> connsToChooseFrom = new ArrayList<>(conns);
            connsToChooseFrom.remove(connection);
            if (connsToChooseFrom.size() == 0) {
                connection.close();
                RoomManager.removeRoom(this);
                return;
            }
            host[0] = connsToChooseFrom.get(0);
        }
        conns.stream().filter(conn -> conn != host[0]).forEach(c -> c.sendPacket(new PlayerLeavePacket(connection.id, false)));
        host[0].sendPacket(new PlayerLeavePacket(connection.id, true));

        RoomManager.removeConnection(this, connection);
        connection.close();
        checkForStart();
    }

    public boolean hasId(int id) {
        return RoomManager.getAllConnections(this).stream().anyMatch(conn -> conn.id == id);
    }
}
