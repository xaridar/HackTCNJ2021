package com.topperbibb.hacktcnj2021.client;

import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteRenderer;
import com.topperbibb.hacktcnj2021.client.game.graphics.Spritesheet;
import com.topperbibb.hacktcnj2021.shared.PlayerLeavePacket;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        User u = new User();
        new Client("hacktcnj-2021.herokuapp.com", 3000, u).connect();
    }
}
