package com.topperbibb.hacktcnj2021.client.game.levels;

import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;
import com.topperbibb.hacktcnj2021.client.game.tiles.Tile;
import com.topperbibb.hacktcnj2021.client.game.tiles.TileInfo;
import com.topperbibb.hacktcnj2021.client.game.tiles.TileTags;
import com.topperbibb.hacktcnj2021.client.game.user.MovableUser;
import com.topperbibb.hacktcnj2021.client.game.user.StaticUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Wall extends Level {

    public Wall(MovableUser movableUser, StaticUser staticUser) {
        super(movableUser, staticUser);
    }

    @Override
    public String[][] setLevel() {
        return new String[][]{
                {"#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#"},
                {"#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#"},
                {"#", "#", "_", "_", "_", "_", "_", "#", "#", "#", "_", "_", "_", "_", "_", "#", "#"},
                {"#", "#", "_", "_", "_", "_", "_", "#", "#", "#", "_", "_", "_", "_", "_", "#", "#"},
                {"#", "#", "_", "_", "_", "_", "_", "#", "#", "#", "_", "_", "_", "_", "_", "#", "#"},
                {"#", "#", "_", "_", "_", "_", "_", "#", "#", "#", "_", "_", "_", "_", "_", "#", "#"},
                {"#", "#", "_", "_", "o", "_", "#", "#", "#", "#", "#", "_", "d", "_", "_", "#", "#"},
                {"#", "#", "_", "_", "_", "_", "_", "#", "#", "#", "_", "_", "_", "_", "_", "#", "#"},
                {"#", "#", "_", "_", "_", "_", "_", "#", "#", "#", "_", "_", "_", "_", "_", "#", "#"},
                {"#", "#", "_", "_", "_", "_", "_", "#", "#", "#", "_", "_", "_", "_", "_", "#", "#"},
                {"#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#"},
                {"#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#"}
        };
    }

    @Override
    public Map<String, Tile> mapObjects() {
        HashMap<String, Tile> map = new HashMap<>();
        map.put( "_", new Tile(new TileInfo(SpriteInfo.sprites.get("Basic_ground"), TileInfo.TileDescriptor.CAN_SPAWN, new ArrayList<>(Collections.singletonList(TileTags.WALKABLE)))));
        map.put( "#", new Tile(new TileInfo(SpriteInfo.sprites.get("Wall"), TileInfo.TileDescriptor.NO_SPAWN, new ArrayList<>(Collections.emptyList()))) );
        map.put( "o", new Tile(new TileInfo(SpriteInfo.sprites.get("Basic_ground"), TileInfo.TileDescriptor.SPAWN_POINT, new ArrayList<>(Collections.singletonList(TileTags.WALKABLE)))));
        map.put( "d", new Tile(new TileInfo(SpriteInfo.sprites.get("Basic_ground"), TileInfo.TileDescriptor.END_POINT, new ArrayList<>(Collections.singletonList(TileTags.WALKABLE)))));
        return map;
    }
}
