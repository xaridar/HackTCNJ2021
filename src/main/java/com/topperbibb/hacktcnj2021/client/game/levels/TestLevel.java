package com.topperbibb.hacktcnj2021.client.game.levels;

import com.topperbibb.hacktcnj2021.client.User;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;
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

    @Override
    public String[][] setLevel() {
        return new String[][]{
                {"#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#"},
                {"#", "o", "_", "_", "_", "_", "_", "#", "#", "#", "_", "_", "_", "_", "_", "_", "#"},
                {"#", "_", "_", "_", "_", "_", "_", "#", "#", "#", "_", "_", "_", "_", "_", "_", "#"},
                {"#", "_", "_", "_", "#", "_", "_", "#", "#", "#", "_", "t", "_", "_", "_", "_", "#"},
                {"#", "_", "_", "_", "#", "_", "_", "_", "_", "#", "_", "_", "_", "_", "#", "_", "#"},
                {"#", "_", "_", "_", "#", "_", "_", "#", "_", "#", "_", "_", "_", "#", "#", "_", "#"},
                {"#", "_", "_", "_", "#", "_", "_", "#", "_", "_", "_", "t", "_", "_", "#", "#", "#"},
                {"#", "_", "t", "_", "#", "_", "_", "#", "#", "_", "_", "_", "_", "_", "#", "#", "#"},
                {"#", "_", "_", "_", "#", "_", "_", "_", "#", "#", "_", "_", "_", "_", "#", "_", "#"},
                {"#", "_", "_", "_", "_", "_", "_", "_", "#", "#", "_", "_", "_", "_", "_", "_", "#"},
                {"#", "_", "_", "_", "_", "_", "_", "_", "_", "#", "_", "_", "_", "_", "_", "d", "#"},
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
        map.put( "_", new Tile(new TileInfo(new SpriteInfo(16, 0, 0), TileInfo.TileDescriptor.NONE, new ArrayList<>(Collections.singletonList(TileTags.WALKABLE)))));
        map.put( "#", new Tile(new TileInfo(new SpriteInfo(16, 128, 0), TileInfo.TileDescriptor.NONE, new ArrayList<>(Collections.emptyList()))) );
        map.put( "o", new Tile(new TileInfo(new SpriteInfo(16, 0, 0), TileInfo.TileDescriptor.SPAWN_POINT, new ArrayList<>(Collections.singletonList(TileTags.WALKABLE)))));
        map.put( "d", new Tile(new TileInfo(new SpriteInfo(16, 96, 48), TileInfo.TileDescriptor.END_POINT, new ArrayList<>(Collections.singletonList(TileTags.WALKABLE)))));
        map.put( "t", new Tile(new TileInfo(new SpriteInfo(16, 0, 0), TileInfo.TileDescriptor.NONE, new ArrayList<>(Collections.singletonList(TileTags.WALKABLE))), new TestObject(new SpriteInfo(16, 80, 48))));
        return map;
    }

    @Override
    public void input(PlayerKeyEvent e) {

    }
}
