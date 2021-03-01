package com.topperbibb.hacktcnj2021.client.game;

import com.topperbibb.hacktcnj2021.client.game.graphics.FlipEnum;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteInfo;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteManager;

import java.util.Map;

public class Config {
    public static void readSprites() {
        SpriteManager.addSprite(new SpriteInfo(16, 32, 88, "Player_right_1"));
        SpriteManager.addSprite(new SpriteInfo(16, 48, 88, "Player_right_2"));
        SpriteManager.addSprite(new SpriteInfo(16, 32, 88, FlipEnum.X, "Player_left_1"));
        SpriteManager.addSprite(new SpriteInfo(16, 48, 88, FlipEnum.X, "Player_left_2"));
        SpriteManager.addSprite(new SpriteInfo(16, 16, 88, "Player_up_1"));
        SpriteManager.addSprite(new SpriteInfo(16, 16, 88, FlipEnum.X, "Player_up_2"));
        SpriteManager.addSprite(new SpriteInfo(16, 0, 88, "Player_down_1"));
        SpriteManager.addSprite(new SpriteInfo(16, 0, 88, FlipEnum.X, "Player_down_2"));

        SpriteManager.addSprite(new SpriteInfo(16, 0, 0, "Basic_ground"));
        SpriteManager.addSprite(new SpriteInfo(16, 128, 0, "Wall"));
        SpriteManager.addSprite(new SpriteInfo(16, 96, 48, "End"));
        SpriteManager.addSprite(new SpriteInfo(16, 80, 48, "Crate"));
        SpriteManager.addSprite(new SpriteInfo(8, 56, 56, "Key"));

        SpriteManager.addSprite(new SpriteInfo(16, 128, 24, "Spawn_point"));

        SpriteManager.defaultSpriteSize = 16;
    }
}
