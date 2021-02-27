package com.topperbibb.hacktcnj2021.client;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    public static List<User> users = new ArrayList<>();

    public static void addUser(User u) {
        users.add(u);
    }

    public static User removeUser(int id) {
        for (User user : users) {
            if (user.id == id) {
                users.remove(user);
                return user;
            }
        }
        return null;
    }
}
