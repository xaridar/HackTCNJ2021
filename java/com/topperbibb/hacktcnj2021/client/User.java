package com.topperbibb.hacktcnj2021.client;

import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.Player;

public class User implements Player {

    // server variables
    int id;
    Client client;
    boolean host;

    // game variables
    int x;
    int y;
    Board board;
    boolean isOverseer;

    public User(){

    }

    public User(int id, boolean host) {
        this.id = id;
        this.host = host;
    }

    public boolean move(int directionX, int directionY) {
        int temp_x = x + directionX;
        int temp_y = y + directionY;
        return true;
    }
}
