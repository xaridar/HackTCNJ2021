package com.topperbibb.hacktcnj2021.client.game;

import com.topperbibb.hacktcnj2021.client.Client;
import com.topperbibb.hacktcnj2021.client.game.user.NetUser;

public class Game {
    public static void main(String[] args) {
        NetUser u = new NetUser();
        Client c = new Client("127.0.0.1", 443, u);
        u.connect(c);
        c.connect();
    }
}
