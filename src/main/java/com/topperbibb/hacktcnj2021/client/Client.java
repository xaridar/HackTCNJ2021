package com.topperbibb.hacktcnj2021.client;

import com.topperbibb.hacktcnj2021.client.game.user.NetUser;
import com.topperbibb.hacktcnj2021.shared.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

/**
 * Represents a local networked client
 * Contains a {@link Socket} connected to the Server
 */
public class Client implements Runnable {

    // The socket host address to connect to
    private final String host;

    // The socket port to connect to
    private final int port;

    // The NetUser associated with this Client, which holds id and host information
    public NetUser user;

    // A local Socket, connected to the Server
    private Socket socket;


    // An InputStream holding all data sent from the connected server
    private DataInputStream in;

    // An OutputStream, which is connected to the server and can send it messages
    private DataOutputStream out;

    // A boolean representing whether this Client is currently accepting incoming messages
    private boolean running = false;

    // A ClientEventListener instance for dealing with incoming Packets
    private ClientEventListener listener;

    /**
     * Initialized with a hostname, port, and local {@link NetUser}
     * @param host the host address to connect to
     * @param port the socket port to connect to on the host
     * @param user the local NetUser instance, which contains id andhost information, as well as game specifics
     */
    public Client(String host, int port, NetUser user) {
        this.host = host;
        this.port = port;
        this.user = user;
    }

    /**
     * Connects to the server if it can
     */
    public void connect() {
        try {
            socket = new Socket(host, port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            listener = new ClientEventListener();
            System.out.println("Connected to the server!");
            new Thread(this).start();
        } catch (ConnectException e) {
            System.out.println("Could not connect to the server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the socket connection
     */
    public void close() {
        try {
            running = false;
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Override from {@link Runnable}
     * Waits for input from {@link #in}, and sends all received data to the connected {@link ClientEventListener}
     */
    @Override
    public void run() {
        running = true;
        try {
            while (running) {
                int len = in.read();
                if (len < 0) continue;
                Packet p = Packet.from(in.readNBytes(len));
                listener.received(p, this);
            }
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a Packet to the server
     * @param p the Packet to send
     */
    public void sendPacket(Packet p) {
        try {
            out.write(p.toByteArray().toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}