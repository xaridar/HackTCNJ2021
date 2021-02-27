package com.topperbibb.hacktcnj2021.client.game;

public class Board {
    Tile[][] board;

    public Board(int sizeX, int sizeY){
        board = new Tile[sizeX][sizeY];
        for(int x = 0; x < sizeX; x++){
            for( int y = 0; y < sizeY; y++){
                board[x][y] = new Tile(x, y);
            }
        }
    }
}
