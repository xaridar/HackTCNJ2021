package com.topperbibb.hacktcnj2021.server;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    public static final Dotenv dotenv = Dotenv.load();

    public static String get(String var) {
        return dotenv.get(var.toUpperCase());
    }
}
