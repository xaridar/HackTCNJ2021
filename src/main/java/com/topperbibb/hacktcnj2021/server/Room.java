package com.topperbibb.hacktcnj2021.server;

import com.topperbibb.hacktcnj2021.shared.EndPacket;
import com.topperbibb.hacktcnj2021.shared.Packet;
import com.topperbibb.hacktcnj2021.shared.PlayerLeavePacket;
import com.topperbibb.hacktcnj2021.shared.StartPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Represents a networked lobby
 */
public class Room {

    // True is the Room is currently in a game
    public boolean playing = false;

    /**
     * Sends a packet to all clients connected to the Room
     * @param p the Packet to send
     */
    public void sendToAll(Packet p) {
        RoomManager.getAllConnections(this).forEach(conn -> conn.sendPacket(p));
    }

    /**
     * Determines whether the lobby can start/continue a game, and sends the appropriate Packet
     */
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

    /**
     * Determines a mover and overseer for the game, then send packets to both clients sharing this information
     */
    private void sendStart() {
        Random random = new Random();
        StartPacket.PlayerType playerType = random.nextInt() == 1 ? StartPacket.PlayerType.OVERSEER : StartPacket.PlayerType.MOVER;
        List<Connection> connectionSet = RoomManager.getAllConnections(this);
        connectionSet.get(0).sendPacket(new StartPacket(playerType));
        connectionSet.get(1).sendPacket(new StartPacket(playerType == StartPacket.PlayerType.OVERSEER ? StartPacket.PlayerType.MOVER : StartPacket.PlayerType.OVERSEER));
    }

    /**
     * Sends all clients a {@link PlayerLeavePacket} informing all players that another player left
     * @param connection the Connection that is disconnecting
     */
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

    /**
     * Determines whether this Room contains any Connections with a given connection id
     * @param id the id to search for among Connections
     * @return true if any associated Connection has {@code id}, false otherwise
     */
    public boolean hasId(int id) {
        return RoomManager.getAllConnections(this).stream().anyMatch(conn -> conn.id == id);
    }
}
