package com.topperbibb.hacktcnj2021.server;

public class Main {
    public static void main(String[] args) {
        String port = System.getenv("PORT");
        if (port == null) {
            port = "3000";
        }
        new Server(Integer.parseInt(port)).start();
    }
}
