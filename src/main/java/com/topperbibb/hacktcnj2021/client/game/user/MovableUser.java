package com.topperbibb.hacktcnj2021.client.game.user;

import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.Engine;
import com.topperbibb.hacktcnj2021.client.game.SfxManager;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteManager;
import com.topperbibb.hacktcnj2021.client.game.objects.BoardObject;
import com.topperbibb.hacktcnj2021.client.game.objects.Key;
import com.topperbibb.hacktcnj2021.client.game.objects.Player;
import com.topperbibb.hacktcnj2021.client.game.objects.RigidBoardObject;
import com.topperbibb.hacktcnj2021.client.game.tiles.Tile;
import com.topperbibb.hacktcnj2021.client.game.tiles.TileTags;

import java.util.Map;

public class MovableUser extends NetUser implements Player {

    // The currently active sprite being used by the player
    private SpriteInfo currentSprite;

    // Specifies types of sprites that users can have
    public enum PlayerSprite {
        DOWN, UP, LEFT, RIGHT,
    }

    public int x;
    public int y;
    Map<PlayerSprite, String> sprites;

    public MovableUser() {

    }

    /**
     * Sets sprites of the user to a map of sprites
     * @param sprites a map of sprites for the user to use
     */
    public MovableUser(Map<PlayerSprite, String> sprites) {
        this.x = 0;
        this.y = 0;
        this.sprites = sprites;
    }

    /**
     * Sets sprites of the user to a map of sprites
     * @param x the starting x coordinate of the user
     * @param y the starting y coordinate of the user
     * @param sprites a map of sprites for the user to use
     */
    public MovableUser(int x, int y, Map<PlayerSprite, String> sprites) {
        this.x = x;
        this.y = y;
        this.sprites = sprites;
    }

    /**
     * Constructor for online games
     * @param id the id to set to the client
     * @param host a boolean representing this user is the host of the room
     */
    public MovableUser(int id, boolean host) {
        super(id, host);
    }

    /**
     * Moves the MovableUser in a specified direction, first checking whether this allowed based on its surrounding Tiles
     * @param directionX the x component of the direction to move
     * @param directionY the y component of the direction to move
     * @return a boolean representing whether the move was successful
     */
    public boolean move(int directionX, int directionY) {
        int temp_x = x + directionX;
        int temp_y = y + directionY;
        Tile tile = Board.board[temp_y][temp_x];
        setNextSprite(directionX, directionY);
        if (tile.hasTag(TileTags.WALKABLE)) {
            if (tile.getObject() != null && tile.getObject() instanceof RigidBoardObject) {
                RigidBoardObject object = (RigidBoardObject) tile.getObject();
                if (object.move(directionX, directionY)) {
                    setPos(temp_x, temp_y);
                    Engine.INSTANCE.applyChanges();
                    return true;
                } else {
                    Engine.INSTANCE.applyChanges();
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
            Engine.INSTANCE.applyChanges();
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
        return SpriteManager.get(sprites.get(spriteEnum));
    }

    public void setSprites(Map<PlayerSprite, String> sprites) {
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

    /**
     * Kills the player and places them at the spawn point, replacing any collected keys
     */
    public void die() {
        SfxManager.playSound("Die");
        if (Board.getSpawnTile().getObject() != null && Board.getSpawnTile().getObject() != this) {
            System.out.println("Spawn covered; resetting level");
            Engine.INSTANCE.getCurrLevel().reset();
            return;
        }
        System.out.println("died");
        for (Key key : Engine.INSTANCE.getCurrLevel().getKeys()) {
            System.out.println("keys replaced");
            Board.board[key.getX()][key.getY()].setObject(key);
        }
        Board.moveObj(Board.board[y][x], Board.getSpawnTile(), getSprite(PlayerSprite.RIGHT));
        Engine.INSTANCE.applyChanges();
    }

    /**
     * Sets the next sprite to show based on direction of motion
     * @param dirX the x coordinate of the direction moved
     * @param dirY the y coordinate of the direction moved
     */
    public void setNextSprite(int dirX, int dirY) {
        MovableUser.PlayerSprite dir = dirX < 0 ? PlayerSprite.LEFT : dirX > 0 ? PlayerSprite.RIGHT : dirY < 0 ? PlayerSprite.UP : PlayerSprite.DOWN;
        setSprite(getSprite(dir));
    }
}
