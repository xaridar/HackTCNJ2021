package com.topperbibb.hacktcnj2021.client.game.levels;

import com.topperbibb.hacktcnj2021.client.game.Board;
import com.topperbibb.hacktcnj2021.client.game.tiles.Tile;
import com.topperbibb.hacktcnj2021.client.game.tiles.TileInfo;
import com.topperbibb.hacktcnj2021.client.game.util.PlayerKeyEvent;

import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class Level {
    private Tile[][] level;
    protected Point spawnPoint;
    protected Point endPoint;

    public Level(){
        level = setLevel();
        setSpawnPoint(setSpawnPoint());
        endPoint = setEndPoint();
        level[endPoint.x][endPoint.y].getInfo().setEndPoint(true);

        level = setObjects(level);

        Board.board = level;
    }

    public abstract Tile[][] setLevel();

    public abstract Point setSpawnPoint();

    public void setSpawnPoint(Point spawnPoint) {
        this.spawnPoint = spawnPoint;
        level[spawnPoint.x][spawnPoint.y].getInfo().setSpawnPoint(true);
    }

    public abstract Point setEndPoint();

    public abstract Tile[][] setObjects(Tile[][] level);

    public abstract void input(PlayerKeyEvent e);
}
