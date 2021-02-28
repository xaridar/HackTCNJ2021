package com.topperbibb.hacktcnj2021.client.game;

import com.topperbibb.hacktcnj2021.client.User;
import com.topperbibb.hacktcnj2021.client.game.levels.Level;
import com.topperbibb.hacktcnj2021.client.game.levels.TestLevel;
import com.topperbibb.hacktcnj2021.client.game.util.PlayerKeyEvent;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Engine implements KeyListener{

    private Level currLevel;

    private static Engine INSTANCE;

    public static Engine getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Engine();
        }

        return INSTANCE;
    }

    public Engine() {
        currLevel = new TestLevel(new User());
    }

    public static void main(String[] args) {
        Engine.getInstance().show();
    }

    public static void show() {
        for(int x = 0; x < Board.board.length; x++) {
            for(int y = 0; y < Board.board[x].length; y++) {
                System.out.print(Board.board[x][y].getSprite());
            }
            System.out.println();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        currLevel.input(new PlayerKeyEvent(e));
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
