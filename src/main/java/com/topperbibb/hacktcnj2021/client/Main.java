package com.topperbibb.hacktcnj2021.client;

public class Main {
    public static void main(String[] args) {
        User u = new User();
        new Client("hacktcnj-2021.herokuapp.com", 443, u).connect();
    }
}
