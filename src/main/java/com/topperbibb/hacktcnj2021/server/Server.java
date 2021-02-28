package com.topperbibb.hacktcnj2021.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server implements Runnable {

    private final int port;
    private ServerSocket serverSocket;
    private boolean running = false;
    public Set<Room> rooms;

    public Server(int port) {
        this.port = port;
        rooms = new HashSet<>();
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        running = true;
        System.out.format("Running on port %d!\n", port);
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                initSocket(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        shutdown();
    }

    public void initSocket(Socket socket) {
        Room room = RoomManager.getNewRoomForConnection();
        RoomManager.addRoom(room);
        Connection connection = new Connection(socket);
        RoomManager.addConnection(room, connection);
        connection.joinRoom(room);
        new Thread(connection).start();
    }

    public void shutdown() {
        running = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
