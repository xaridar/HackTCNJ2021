package com.topperbibb.hacktcnj2021.client;

public class Main {
    public static void main(String[] args) {
        User u = new User();
        new Client("127.0.0.1", 9200, u).connect();
    }
}
