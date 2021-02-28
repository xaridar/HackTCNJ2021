package com.topperbibb.hacktcnj2021.client.game.levels;

import com.topperbibb.hacktcnj2021.client.User;
import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.objects.TestObject;
import com.topperbibb.hacktcnj2021.client.game.tiles.Tile;
import com.topperbibb.hacktcnj2021.client.game.util.PlayerKeyEvent;

import java.awt.*;

public class TestLevel extends Level{

    User player;

    @Override
    public Tile[][] setLevel() {
        return Board.loadBoard(new String[][]{
                {"#", "#", "#", "#", "#", "#", "#", "#", "#", "#"},
                {"#", "_", "_", "_", "_", "_", "_", "_", "_", "#"},
                {"#", "_", "_", "_", "_", "_", "_", "_", "_", "#"},
                {"#", "_", "_", "_", "_", "_", "_", "_", "_", "#"},
                {"#", "_", "_", "_", "_", "_", "_", "_", "_", "#"},
                {"#", "_", "_", "_", "_", "_", "_", "_", "_", "#"},
                {"#", "_", "_", "_", "_", "_", "_", "_", "_", "#"},
                {"#", "_", "_", "_", "_", "_", "_", "_", "_", "#"},
                {"#", "_", "_", "_", "_", "_", "_", "_", "_", "#"},
                {"#", "_", "_", "_", "_", "_", "_", "_", "_", "#"},
                {"#", "#", "#", "#", "#", "#", "#", "#", "#", "#"}
        });
    }

    public TestLevel(User player) {
        super();
        this.player = player;
    }

    @Override
    public Point setSpawnPoint() {
        return new Point(1, 1);
    }

    @Override
    public Point setEndPoint() {
        return new Point(9, 8);
    }

    @Override
    public Tile[][] setObjects(Tile[][] level) {
        level[2][2].setObject(new TestObject("t"));
        return level;
    }

    @Override
    public void input(PlayerKeyEvent e) {
        if(e.getKeyChar() == 'w') {
            player.move(0, -1);
        }else if(e.getKeyChar() == 'a') {
            player.move(-1, 0);
        }else if(e.getKeyChar() == 's') {
            player.move(1, 0);
        }else if(e.getKeyChar() == 'd') {
            player.move(0, 1);
        }
    }


}
