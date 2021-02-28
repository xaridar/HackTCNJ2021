package com.topperbibb.hacktcnj2021.client.game;

import com.topperbibb.hacktcnj2021.client.game.graphics.FlipEnum;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;

import java.util.List;
import java.util.Map;

public class Config {
    public static void readSprites() {
        final Map<String, SpriteInfo> sprites = SpriteInfo.sprites;

        sprites.put("Player_right_1", new SpriteInfo(16, 32, 88));
        sprites.put("Player_right_2", new SpriteInfo(16, 48, 88));
        sprites.put("Player_left_1", new SpriteInfo(16, 32, 88, FlipEnum.X));
        sprites.put("Player_left_2", new SpriteInfo(16, 48, 88, FlipEnum.X));
        sprites.put("Player_up_1", new SpriteInfo(16, 16, 88));
        sprites.put("Player_up_2", new SpriteInfo(16, 16, 88, FlipEnum.X));
        sprites.put("Player_down_1", new SpriteInfo(16, 0, 88));
        sprites.put("Player_down_2", new SpriteInfo(16, 0, 88, FlipEnum.X));

        sprites.put("Basic_ground", new SpriteInfo(16, 0, 0));
        sprites.put("Wall", new SpriteInfo(16, 128, 0));
        sprites.put("End", new SpriteInfo(16, 96, 48));
        sprites.put("Crate", new SpriteInfo(16, 80, 48));

        sprites.put("Spawn_point", new SpriteInfo(16, 128, 24));

        SpriteInfo.defaultSpriteSize = 16;
    }
}
