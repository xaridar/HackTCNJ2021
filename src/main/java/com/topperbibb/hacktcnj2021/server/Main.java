package com.topperbibb.hacktcnj2021.server;

/**
 * A simple main class for connecting the Server
 */
public class Main {
    public static void main(String[] args) {
        String port = System.getenv("PORT");
        if (port == null) {
            port = "443";
        }
        new Server(Integer.parseInt(port)).start();
    }
}
