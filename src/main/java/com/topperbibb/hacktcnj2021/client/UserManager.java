package com.topperbibb.hacktcnj2021.client;

import com.topperbibb.hacktcnj2021.client.game.user.NetUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages local players in a client's room
 */
public class UserManager {

    /**
     * Contains all {@link NetUser}s in a player's room
     */
    public static List<NetUser> users = new ArrayList<>();

    /**
     * Adds a user locally to {@link #users}
     * @param u a NetUser to add
     */
    public static void addUser(NetUser u) {
        users.add(u);
    }

    /**
     * Removes a user locally from {@link #users}
     * @param id the id of the NetUser to remove
     * @return the NetUser removed, or null if no user is found with id {@code id}
     */
    public static NetUser removeUser(int id) {
        for (NetUser user : users) {
            if (user.id == id) {
                users.remove(user);
                return user;
            }
        }
        return null;
    }
}
