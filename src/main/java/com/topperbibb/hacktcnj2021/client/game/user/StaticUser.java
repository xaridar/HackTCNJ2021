package com.topperbibb.hacktcnj2021.client.game.user;

/**
 * Represents the game's overseer
 */
public class StaticUser extends NetUser{

    /**
     * Constructor either takes no parameters or an id and host boolean for network connections
     * @param id the id to set to the client
     * @param host a boolean representing this user is the host of the room
     */
    public StaticUser(int id, boolean host) {
        super(id, host);
    }

    public StaticUser() {}
}
