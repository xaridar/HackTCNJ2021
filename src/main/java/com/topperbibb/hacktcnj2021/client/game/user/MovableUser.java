package com.topperbibb.hacktcnj2021.client.game.user;

import com.topperbibb.hacktcnj2021.client.Client;
import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.Engine;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;
import com.topperbibb.hacktcnj2021.client.game.graphics.Spritesheet;
import com.topperbibb.hacktcnj2021.client.game.objects.BoardObject;
import com.topperbibb.hacktcnj2021.client.game.objects.Key;
import com.topperbibb.hacktcnj2021.client.game.objects.Player;
import com.topperbibb.hacktcnj2021.client.game.objects.RigidBoardObject;
import com.topperbibb.hacktcnj2021.client.game.tiles.Tile;
import com.topperbibb.hacktcnj2021.client.game.tiles.TileTags;

import java.util.Map;

public class MovableUser extends NetUser implements Player {

    private SpriteInfo currentSprite;

    public enum PlayerSprite {
        DOWN(0), UP(1), LEFT(2), RIGHT(3),
        DOWN2(4), UP2(5), LEFT2(6), RIGHT2(7);

        public int i;
        PlayerSprite(int i) {
            this.i = i;
        }

        public static PlayerSprite fromInt(int i) {
            for (PlayerSprite val : values()) {
                if (val.i == i) {
                    return val;
                }
            }
            return null;
        }
    }

    // game variables
    public int x;
    public int y;
    Map<PlayerSprite, SpriteInfo> sprites;
    boolean isOverseer;

    public MovableUser() {

    }

    public MovableUser(Map<PlayerSprite, SpriteInfo> sprites) {
        this.x = 0;
        this.y = 0;
        this.sprites = sprites;
    }

    public MovableUser(int x, int y, Map<PlayerSprite, SpriteInfo> sprites) {
        this.x = x;
        this.y = y;
        this.sprites = sprites;
    }

    public MovableUser(int id, boolean host) {
        super(id, host);
    }

    public boolean move(int directionX, int directionY) {
        int temp_x = x + directionX;
        int temp_y = y + directionY;
        Tile tile = Board.board[temp_y][temp_x];
        if (tile.hasTag(TileTags.WALKABLE)) {
            if (tile.getObject() != null && tile.getObject() instanceof RigidBoardObject) {
                RigidBoardObject object = (RigidBoardObject) tile.getObject();
                if (object.move(directionX, directionY)) {
                    setPos(temp_x, temp_y);
                    Engine.INSTANCE.applyChanges();
                    return true;
                } else {
                    return false;
                }
            } else if (tile.getObject() != null && tile.getObject() instanceof Key) {
                System.out.println("true");
                ((Key) tile.getObject()).collect();
            }
            setPos(temp_x, temp_y);
            Engine.INSTANCE.applyChanges();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public SpriteInfo getSprite() {
        return currentSprite;
    }

    @Override
    public void setSprite(SpriteInfo sprite) {
        this.currentSprite = sprite;
    }

    public SpriteInfo getSprite(PlayerSprite spriteEnum) {
        return sprites.get(spriteEnum);
    }

    public void setSprite(PlayerSprite which, SpriteInfo sprite) {
        sprites.put(which, sprite);
    }

    public void setSprites(Map<PlayerSprite, SpriteInfo> sprites) {
        this.sprites = sprites;
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
        Board.board[this.y][this.x].setObject(null);
        this.x = x;
        this.y = y;
        Board.board[y][x].setObject(this);
    }

    @Override
    public BoardObject copy() {
        return new MovableUser(x, y, sprites);
    }

    public boolean isOverseer() {
        return isOverseer;
    }

    public void setOverseer(boolean overseer) {
        isOverseer = overseer;
    }

    public void die() {
        if (Board.getSpawnTile().getObject() != null) {
            System.out.println("Spawn covered; resetting level");
            Engine.INSTANCE.getCurrLevel().reset();
            return;
        }
        System.out.println("died");
        System.out.println(Engine.INSTANCE.getCurrLevel().getKeys());
        System.out.println(Engine.loadOrder[Engine.loadIndex]);
        for (Key key : Engine.INSTANCE.getCurrLevel().getKeys()) {
            System.out.println("keys replaced");
            Board.board[key.getX()][key.getY()].setObject(key);
        }
        System.out.println(Board.getSpawnTile().getY() + ", " + Board.getSpawnTile().getX());
        Board.moveObj(Board.board[y][x], Board.getSpawnTile());
    }
}
