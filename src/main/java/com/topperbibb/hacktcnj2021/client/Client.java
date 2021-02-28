package com.topperbibb.hacktcnj2021.client;

import com.topperbibb.hacktcnj2021.shared.*;

import java.io.*;
import java.net.Socket;
import java.net.ConnectException;

public class Client implements Runnable {

    private final String host;
    private final int port;
    public final User user;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean running = false;

    private ClientEventListener listener;

    public Client(String host, int port, User user) {
        this.host = host;
        this.port = port;
        this.user = user;
    }

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

    public void sendPacket(Packet p) {
        try {
            out.write(p.toByteArray().toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}