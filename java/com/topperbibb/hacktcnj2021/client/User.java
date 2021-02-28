package com.topperbibb.hacktcnj2021.client;

import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.objects.Player;
import com.topperbibb.hacktcnj2021.client.game.tiles.TileTags;

public class User implements Player {

    // server variables
    int id;
    Client client;
    boolean host;

    // game variables
    int x;
    int y;
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
        if(Board.board[temp_x][temp_y].hasTag(TileTags.WALKABLE)) {
            x = temp_x;
            y = temp_y;
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String getSprite() {
        return null;
    }

    @Override
    public void setSprite(String sprite) {

    }
}
