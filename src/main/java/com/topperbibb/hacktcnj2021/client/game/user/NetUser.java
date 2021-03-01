package com.topperbibb.hacktcnj2021.client.game.user;

import com.topperbibb.hacktcnj2021.client.Client;
import com.topperbibb.hacktcnj2021.shared.Packet;

public class NetUser {

    public int id;
    public boolean host;
    public Client client;

    public NetUser() {

    }

    public NetUser(int id, boolean host) {
        this.id = id;
        this.host = host;
    }

    public void connect(Client c) {
        this.client = c;
    }

    public MovableUser asMovable() {
        MovableUser u = new MovableUser(id, host);
        u.connect(client);
        return u;
    }

    public StaticUser asStatic() {
        StaticUser u = new StaticUser(id, host);
        u.connect(client);
        return u;
    }

    public void sendPacket(Packet p) {
        client.sendPacket(p);
    }
}
