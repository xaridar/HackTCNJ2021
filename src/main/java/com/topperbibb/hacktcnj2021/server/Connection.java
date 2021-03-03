package com.topperbibb.hacktcnj2021.server;

import com.topperbibb.hacktcnj2021.shared.ConnectPacket;
import com.topperbibb.hacktcnj2021.shared.Packet;
import com.topperbibb.hacktcnj2021.shared.PingPacket;
import com.topperbibb.hacktcnj2021.shared.PlayerJoinPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Handles a single connection to a client
 */
public class Connection implements Runnable {

    // This connection's unique id
    public final int id;

    // A boolean representing whether the client associated with this connection is the host
    public boolean host;

    // A socket for client-server communication
    private Socket socket;

    // The Room that holds this Connection
    private Room room;

    // An InputStream holding all data sent from the connected client
    private DataInputStream in;

    // An OutputStream, which is connected to the client and can send it messages
    private DataOutputStream out;

    // A ServerEventListener instance for handling Packets
    private ServerEventListener listener;


    // A boolean representing whether this connection is waiting for its associated client to send back a PING message
    public boolean lookingForPong;

    // A separate Thread which handles sending PONG messages
    public Runnable pingThread = () -> {

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            return;
        }
        lookingForPong = true;
        sendPacket(new PingPacket());
        System.out.println("PING sent!");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            return;
        }
        if (socket.isClosed()) return;
        lookingForPong = false;
        room.sendPlayerLeave(Connection.this);
    };

    // An ExecutorService managing the pingThread
    private ExecutorService exec = Executors.newFixedThreadPool(1);

    private Future<?> threadFuture;

    /**
     * Initializes a new Connection from a Socket
     * @param socket a Socket connected to a client
     */
    public Connection(Socket socket) {
        this.socket = socket;
        System.out.format("Connected to client at ip %s\n", socket.getInetAddress());
        id = IDManager.generateID();
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            listener = new ServerEventListener();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Joins a given Room
     * @param room the Room to join
     */
    public void joinRoom(Room room) {
        this.room = room;
        host = RoomManager.getAllConnections(room).stream().noneMatch(conn -> conn.host);
        sendPacket(new ConnectPacket(id, host));
        RoomManager.getAllConnections(room).forEach(conn -> conn.sendPacket(new PlayerJoinPacket(id, host)));
        for (Connection c : RoomManager.getAllConnections(room)) {
            if (c != this) {
                sendPacket(new PlayerJoinPacket(c.id, c.host));
            }
        }
        this.room.checkForStart();
        resetPing();
    }

    /**
     * Override from {@link Runnable}
     * Waits for input from {@link #in}, and sends all received data to the connected {@link ServerEventListener}
     */
    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                if (in.available() > 0) {
                    int len = in.read();
                    Packet p = Packet.from(in.readNBytes(len));
                    if (p == null) continue;
                    listener.received(p, this, room);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a Packet to the connected client
     * @param p the Packet to send
     */
    public void sendPacket(Packet p) {
        try {
            System.out.println("Sent packet");
            out.write(p.toByteArray().toByteArray());
        } catch (SocketException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Restarts the PING/PONG Thread for ensuring consistent connection to clients
     */
    public void resetPing() {
        if (threadFuture != null && !threadFuture.isDone()) {
            exec.shutdownNow();
            exec = Executors.newFixedThreadPool(1);
        }
        if (socket.isClosed()) return;

        lookingForPong = false;

        threadFuture = exec.submit(pingThread);
    }

    /**
     * Closes the connection
     */
    public void close() {
        exec.shutdownNow();
        try {
            socket.close();
            in.close();
            out.close();
            System.out.format("Disconnected from client at ip %s\n", socket.getInetAddress());
            IDManager.addAvailableId(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
