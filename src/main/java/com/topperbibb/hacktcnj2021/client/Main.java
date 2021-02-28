package com.topperbibb.hacktcnj2021.client;

import com.topperbibb.hacktcnj2021.client.game.user.MovableUser;
import com.topperbibb.hacktcnj2021.client.game.user.NetUser;

public class Main {
    public static void main(String[] args) {
        NetUser u = new NetUser();
        new Client("127.0.0.1", 443, u).connect();
    }
}
