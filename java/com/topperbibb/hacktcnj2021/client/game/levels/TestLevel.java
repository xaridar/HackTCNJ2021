package com.topperbibb.hacktcnj2021.client.game.levels;

import com.topperbibb.hacktcnj2021.client.User;
import com.topperbibb.hacktcnj2021.client.game.graphics.Spritesheet;
import com.topperbibb.hacktcnj2021.client.game.objects.TestObject;
import com.topperbibb.hacktcnj2021.client.game.tiles.Tile;
import com.topperbibb.hacktcnj2021.client.game.tiles.TileInfo;
import com.topperbibb.hacktcnj2021.client.game.tiles.TileTags;
import com.topperbibb.hacktcnj2021.client.game.util.PlayerKeyEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TestLevel extends Level{

    User player;
    Spritesheet spritesheet;

    @Override
    public String[][] setLevel() {
        return new String[][]{
                {"#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#"},
                {"#", "o", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "#"},
                {"#", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "#"},
                {"#", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "#"},
                {"#", "_", "_", "t", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "#"},
                {"#", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "#"},
                {"#", "_", "_", "_", "_", "_", "_", "d", "_", "_", "_", "_", "_", "_", "_", "_", "#"},
                {"#", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "#"},
                {"#", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "#"},
                {"#", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "#"},
                {"#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#"}
        };
    }

    public TestLevel(User player) {
        super();
        this.player = player;
    }

    @Override
    public Map<String, Tile> mapObjects() {
        HashMap<String, Tile> map = new HashMap<>();
        map.put( "_", new Tile(new TileInfo("_ ", TileInfo.TileDescriptor.NONE, new ArrayList<>(Collections.singletonList(TileTags.WALKABLE)))));
        map.put( "#", new Tile(new TileInfo("# ", TileInfo.TileDescriptor.NONE, new ArrayList<>(Collections.emptyList()))) );
        map.put( "o", new Tile(new TileInfo("o ", TileInfo.TileDescriptor.SPAWN_POINT, new ArrayList<>(Collections.singletonList(TileTags.WALKABLE)))));
        map.put( "d", new Tile(new TileInfo("d ", TileInfo.TileDescriptor.END_POINT, new ArrayList<>(Collections.singletonList(TileTags.WALKABLE)))));
        map.put( "t", new Tile(new TileInfo("_ ", TileInfo.TileDescriptor.NONE, new ArrayList<>(Collections.singletonList(TileTags.WALKABLE))), new TestObject("t ")));
        return map;
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
