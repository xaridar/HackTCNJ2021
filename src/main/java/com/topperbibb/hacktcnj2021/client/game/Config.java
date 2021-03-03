package com.topperbibb.hacktcnj2021.client.game;

import com.topperbibb.hacktcnj2021.client.game.graphics.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Config {
    public static void readSprites() {
        SpriteJson sprites;
        try {
            JSONTokener tokener = new JSONTokener(Engine.class.getResourceAsStream("/config.json"));
            JSONObject object = new JSONObject(tokener);
            sprites = new SpriteJson();
            if (!object.isEmpty()) {
                sprites = SpriteJson.createSpriteJson(object);
            }
            SpriteManager.spritesheet = new Spritesheet(sprites.spriteSheetPath);
            for (SpriteJson.JsonSprite sprite : sprites.sprites) {
                SpriteManager.addSprite(new SpriteInfo(sprite.width, sprite.height, sprite.x, sprite.y, sprite.flipped, sprite.name, SpriteManager.spritesheet, sprite.pixelScale));
            }
            for (Triplet<String, List<SpriteJson.JsonSprite>, Boolean> set : sprites.spriteSets) {
                List<SpriteInfo> infoList = set.getSecond().stream().map(jsonSprite -> new SpriteInfo(jsonSprite.width, jsonSprite.height, jsonSprite.x, jsonSprite.y, jsonSprite.flipped, jsonSprite.name, SpriteManager.spritesheet, jsonSprite.pixelScale)).collect(Collectors.toList());
                SpriteManager.addSprite(new SpriteSet(infoList, set.getFirst(), set.getThird()));
            }
        } catch (NullPointerException | JSONException | ClassCastException e) {
            sprites = null;
        }

        Spritesheet defaultSpritesheet = new Spritesheet("/defaulttiles.png");

        SpriteManager.addSpriteDefault(new SpriteSet(Arrays.asList(new SpriteInfo(16, 16, 32, 88, "Player_right", defaultSpritesheet), new SpriteInfo(16, 16, 48, 88, "Player_right", defaultSpritesheet)), "Player_right"));
        SpriteManager.addSpriteDefault(new SpriteSet(Arrays.asList(new SpriteInfo(16, 16, 32, 88, FlipEnum.X,"Player_left", defaultSpritesheet), new SpriteInfo(16, 16, 48, 88, FlipEnum.X,"Player_left", defaultSpritesheet)), "Player_left"));
        SpriteManager.addSpriteDefault(new SpriteSet(Arrays.asList(new SpriteInfo(16, 16, 16, 88, "Player_up", defaultSpritesheet), new SpriteInfo(16, 16, 16, 88, FlipEnum.X, "Player_up", defaultSpritesheet)), "Player_up"));
        SpriteManager.addSpriteDefault(new SpriteSet(Arrays.asList(new SpriteInfo(16, 16, 0, 88, "Player_down", defaultSpritesheet), new SpriteInfo(16, 16, 0, 88, FlipEnum.X,"Player_down", defaultSpritesheet)), "Player_down"));

        SpriteManager.addSpriteDefault(new SpriteInfo(16, 16, 0, 0, "Basic_ground", defaultSpritesheet));
        SpriteManager.addSpriteDefault(new SpriteInfo(16, 16, 128, 0, "Wall", defaultSpritesheet));
        SpriteManager.addSpriteDefault(new SpriteInfo(16, 16, 96, 48, "End", defaultSpritesheet));
        SpriteManager.addSpriteDefault(new SpriteInfo(16, 16, 80, 48, "Crate", defaultSpritesheet));
        SpriteManager.addSpriteDefault(new SpriteInfo(8, 8, 56, 56, "Key", defaultSpritesheet));

        SpriteManager.addSpriteDefault(new SpriteInfo(16, 16, 128, 24, "Spawn_point", defaultSpritesheet));

        if (sprites != null) {
            SpriteManager.tileSize = sprites.tileSize;
            if (sprites.tileSize < 4) {
                SpriteManager.tileSize = 4;
            }
            SpriteManager.pixelScale = sprites.pixelScale;
        } else {
            SpriteManager.tileSize = 16;
            SpriteManager.pixelScale = 4;
        }
    }
}
