package com.topperbibb.hacktcnj2021.client.game;

import com.topperbibb.hacktcnj2021.client.Client;
import com.topperbibb.hacktcnj2021.client.game.user.MovableUser;
import com.topperbibb.hacktcnj2021.client.game.user.NetUser;

/**
 * Main class, which will start a game in either single- or multi-player mode
 */
public class Game {
    public static void main(String[] args) {
         createMultiplayer();
    }

    /**
     * Creates a new {@link MovableUser} and attaches it to a single player {@link Engine} instance
     */
    private static void createSinglePlayer() {
        MovableUser u = new MovableUser();
        Engine.startSingleplayerEngine(u);
    }

    /**
     * Creates a {@link Client} instance and tries to connect it to the server
     */
    public static void createMultiplayer() {
        NetUser u = new NetUser();
        Client c = new Client("127.0.0.1", 443, u);
        u.connect(c);
        c.connect();
    }
}
