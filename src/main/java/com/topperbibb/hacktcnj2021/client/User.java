package com.topperbibb.hacktcnj2021.client;

import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;
import com.topperbibb.hacktcnj2021.client.game.graphics.Spritesheet;
import com.topperbibb.hacktcnj2021.client.game.objects.Player;
import com.topperbibb.hacktcnj2021.client.game.tiles.TileTags;

public class User implements Player {

    // server variables
    int id;
    Client client;
    boolean host;

    // game variables
    public int x;
    public int y;
    SpriteInfo sprite;
    boolean isOverseer;

    public User(){

    }

    public User(int x, int y, SpriteInfo sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    public User(int id, boolean host) {
        this.id = id;
        this.host = host;
    }

    public boolean move(int directionX, int directionY) {
        int temp_x = x + directionX;
        int temp_y = y + directionY;
        if(Board.board[temp_y][temp_x].hasTag(TileTags.WALKABLE)) {
            x = temp_x;
            y = temp_y;
            return true;
        }else{
            return false;
        }
    }

    @Override
    public SpriteInfo getSprite() {
        return sprite;
    }

    @Override
    public void setSprite(SpriteInfo sprite) {
        this.sprite = sprite;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
