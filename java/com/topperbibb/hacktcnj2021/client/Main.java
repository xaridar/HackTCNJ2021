package com.topperbibb.hacktcnj2021.client;

import com.topperbibb.hacktcnj2021.shared.PlayerLeavePacket;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        User u = new User();
        Client client = new Client("127.0.0.1", 9200, u);
        client.connect();
        Scanner scanner = new Scanner(System.in);
        if (scanner.next().equals("leave")) {
            client.sendPacket(new PlayerLeavePacket(u.id, false));
        }
    }
}
