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
import java.util.Timer;
import java.util.TimerTask;

public class Connection implements Runnable {

    public final int id;
    public boolean host;

    private final Socket socket;
    private Room room;

    private DataInputStream in;
    private DataOutputStream out;
    private ServerEventListener listener;

    public boolean lookingForPong;
    public Thread pingThread;

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

    public void sendPacket(Packet p) {
        try {
            out.write(p.toByteArray().toByteArray());
        } catch (SocketException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetPing() {
        if (pingThread != null) {
            pingThread.stop();
        }
        if (socket.isClosed()) return;

        lookingForPong = false;
        pingThread = new Thread(() -> {

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lookingForPong = true;
            sendPacket(new PingPacket());
            System.out.println("PING sent!");

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (socket.isClosed()) return;
            lookingForPong = false;
            room.sendPlayerLeave(Connection.this);
            pingThread.stop();
        });
        pingThread.start();

    }

    public void close() {
        if (pingThread != null) {
            pingThread.stop();
        }
        try {
            socket.close();
            in.close();
            out.close();
            System.out.format("Disconnected from client at ip %s\n", socket.getInetAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
