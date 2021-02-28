package com.topperbibb.hacktcnj2021.client.game.levels;

import com.topperbibb.hacktcnj2021.client.game.user.MovableUser;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;
import com.topperbibb.hacktcnj2021.client.game.objects.TestObject;
import com.topperbibb.hacktcnj2021.client.game.tiles.Tile;
import com.topperbibb.hacktcnj2021.client.game.tiles.TileInfo;
import com.topperbibb.hacktcnj2021.client.game.tiles.TileTags;
import com.topperbibb.hacktcnj2021.client.game.user.StaticUser;
import com.topperbibb.hacktcnj2021.client.game.util.PlayerKeyEvent;

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

    public TestLevel(MovableUser movableUser, StaticUser staticUser) {
        super();
        this.movableUser = movableUser;
        this.staticUser = staticUser;
    }

    @Override
    public Map<String, Tile> mapObjects() {
        HashMap<String, Tile> map = new HashMap<>();
        map.put( "_", new Tile(new TileInfo(SpriteInfo.sprites.get("Basic_ground"), TileInfo.TileDescriptor.CAN_SPAWN, new ArrayList<>(Collections.singletonList(TileTags.WALKABLE)))));
        map.put( "#", new Tile(new TileInfo(SpriteInfo.sprites.get("Wall"), TileInfo.TileDescriptor.NO_SPAWN, new ArrayList<>(Collections.emptyList()))) );
        map.put( "o", new Tile(new TileInfo(SpriteInfo.sprites.get("Basic_ground"), TileInfo.TileDescriptor.SPAWN_POINT, new ArrayList<>(Collections.singletonList(TileTags.WALKABLE)))));
        map.put( "d", new Tile(new TileInfo(SpriteInfo.sprites.get("End"), TileInfo.TileDescriptor.END_POINT, new ArrayList<>(Collections.singletonList(TileTags.WALKABLE)))));
        map.put( "t", new Tile(new TileInfo(SpriteInfo.sprites.get("Basic_ground"), TileInfo.TileDescriptor.NO_SPAWN, new ArrayList<>(Collections.singletonList(TileTags.WALKABLE))), new TestObject(SpriteInfo.sprites.get("Crate"))));
        return map;
    }

    @Override
    public void input(PlayerKeyEvent e) {

    }
}
