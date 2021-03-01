package com.topperbibb.hacktcnj2021.client.game.objects;

import com.topperbibb.hacktcnj2021.client.game.objects.BoardObject;

/**
 * An interface for non-static board objects
 * Examples:
 *  Player
 *  Pushable Boxes
 */
public interface RigidBoardObject extends BoardObject {

    boolean move(int directionX, int directionY);

}
