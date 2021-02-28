package com.topperbibb.hacktcnj2021.client.game;

import com.topperbibb.hacktcnj2021.client.game.tiles.Tile;
import com.topperbibb.hacktcnj2021.client.game.tiles.TileInfo;

public class Board {
    public static Tile[][] board;

    public static Tile[][] loadBoard(String[][] arrayBoard) {
        Tile[][] tempBoard = new Tile[arrayBoard.length][arrayBoard[0].length];
        for(int x = 0; x < tempBoard.length; x++) {
            for(int y = 0; y < tempBoard[x].length; y++) {
                tempBoard[x][y] = new Tile(x, y, new TileInfo(arrayBoard[x][y], false));
            }
        }
        return tempBoard;
    }
}
