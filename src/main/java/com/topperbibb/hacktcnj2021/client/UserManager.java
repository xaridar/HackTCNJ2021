package com.topperbibb.hacktcnj2021.client;

import com.topperbibb.hacktcnj2021.client.game.user.NetUser;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    public static List<NetUser> users = new ArrayList<>();

    public static void addUser(NetUser u) {
        users.add(u);
    }

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
