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
    private Timer pingTimer;

    Runnable ping = new Runnable() {

        @Override
        public void run() {
            lookingForPong = true;
            sendPacket(new PingPacket());
            System.out.println("PING sent!");

            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            lookingForPong = false;
            room.sendPlayerLeave(Connection.this);
        }
    };

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
        host = RoomManager.getAllConnections(room).size() == 0;
        sendPacket(new ConnectPacket(id, host));
        RoomManager.getAllConnections(room).forEach(conn -> conn.sendPacket(new PlayerJoinPacket(id, host)));
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
        if (pingTimer != null) {
            pingTimer.cancel();
        }

        lookingForPong = false;
        pingTimer = new Timer();
        pingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                ping.run();
            }
        }, 30000);

    }

    public void close() {
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
