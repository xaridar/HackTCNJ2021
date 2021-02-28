package com.topperbibb.hacktcnj2021.client.game.objects;

import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.Engine;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;
import com.topperbibb.hacktcnj2021.client.game.tiles.TileTags;

public class TestObject implements RigidBoardObject{

    SpriteInfo sprite;
    int x, y;

    public TestObject(SpriteInfo sprite) {
        setSprite(sprite);
    }

    @Override
    public SpriteInfo getSprite() {
        return sprite;
    }

    public void setSprite(SpriteInfo sprite) {
        this.sprite = sprite;
    }

    @Override
    public boolean move(int directionX, int directionY) {
        int temp_x = x + directionY;
        int temp_y = y + directionX;
        if(Board.board[temp_x][temp_y].hasTag(TileTags.WALKABLE)) {
            if(Board.board[temp_x][temp_y].getObject()==null) {
                Board.board[x][y].setObject(null);
                x = temp_x;
                y = temp_y;
                Board.board[x][y].setObject(this);
                return true;
            }
        }
        return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public BoardObject copy() {
        return new TestObject(sprite);
    }
}
