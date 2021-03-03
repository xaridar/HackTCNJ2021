package com.topperbibb.hacktcnj2021.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Contains the code for a websocket server, given a port number. Receives new clients on a separate thread, and spins up new threads for each {@link Connection}.
 */
public class Server implements Runnable {

    // The desired socket port
    private final int port;

    // Holds all connected sockets
    private ServerSocket serverSocket;

    // Determines whether the server is accepting clients
    private boolean running = false;

    /**
     * Initializes a new Server and starts a websocket server.
     * @param port the desired socket port for the server to listen on.
     */
    public Server(int port) {
        this.port = port;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the server
     */
    public void start() {
        new Thread(this).start();
    }

    /**
     * Override from {@link Runnable}
     * Waits for new connections on a new thread, then initializes each one as they connect.
     */
    @Override
    public void run() {
        running = true;
        System.out.format("Running at %s, port %d!\n", serverSocket.getLocalSocketAddress(), port);
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

    /**
     * Initializes a socket by creating a {@link Connection}, assigning it to a room, and starting its Thread.
     * @param socket a socket spit out by the ServerSocket
     */
    public void initSocket(Socket socket) {
        Room room = RoomManager.getNewRoomForConnection();
        RoomManager.addRoom(room);
        Connection connection = new Connection(socket);
        RoomManager.addConnection(room, connection);
        connection.joinRoom(room);
        new Thread(connection).start();
    }

    /**
     * Shuts down the server when the socket closes or errors.
     */
    public void shutdown() {
        running = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
