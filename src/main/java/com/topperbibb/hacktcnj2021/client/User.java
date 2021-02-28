package com.topperbibb.hacktcnj2021.client;

import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;
import com.topperbibb.hacktcnj2021.client.game.graphics.Spritesheet;
import com.topperbibb.hacktcnj2021.client.game.objects.Player;
import com.topperbibb.hacktcnj2021.client.game.objects.RigidBoardObject;
import com.topperbibb.hacktcnj2021.client.game.tiles.TileTags;

import java.util.Map;

public class User implements Player {

    public enum PlayerSprite {
        DOWN, UP, LEFT, RIGHT,
        DOWN2, UP2, LEFT2, RIGHT2,
    }

    // server variables
    int id;
    Client client;
    boolean host;

    // game variables
    public int x;
    public int y;
    Map<PlayerSprite, SpriteInfo> sprites;
    boolean isOverseer;

    public User(){

    }

    public User(int x, int y, Map<PlayerSprite, SpriteInfo> sprites) {
        this.x = x;
        this.y = y;
        this.sprites = sprites;
    }

    public User(int id, boolean host) {
        this.id = id;
        this.host = host;
    }

    public boolean move(int directionX, int directionY) {
        int temp_x = x + directionX;
        int temp_y = y + directionY;
        if(Board.board[temp_y][temp_x].hasTag(TileTags.WALKABLE)) {
            if(Board.board[temp_y][temp_x].getObject() != null && Board.board[temp_y][temp_x].getObject() instanceof RigidBoardObject) {
                RigidBoardObject object = (RigidBoardObject)Board.board[temp_y][temp_x].getObject();
                if(object.move(directionX, directionY)) {
                    x = temp_x;
                    y = temp_y;
                    return true;
                }
                else {
                    return false;
                }
            }
            x = temp_x;
            y = temp_y;
            return true;
        }else{
            return false;
        }
    }

    @Override
    public SpriteInfo getSprite() {
        return sprites.get(PlayerSprite.RIGHT);
    }

    public SpriteInfo getSprite(PlayerSprite spriteEnum) {
        return sprites.get(spriteEnum);
    }

    public void setSprite(PlayerSprite which, SpriteInfo sprite) {
        sprites.put(which, sprite);
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
