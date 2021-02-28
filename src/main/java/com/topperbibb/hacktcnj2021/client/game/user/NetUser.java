package com.topperbibb.hacktcnj2021.client.game.user;

import com.topperbibb.hacktcnj2021.client.Client;

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
}
