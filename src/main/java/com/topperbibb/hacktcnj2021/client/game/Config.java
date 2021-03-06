package com.topperbibb.hacktcnj2021.client.game;

import com.topperbibb.hacktcnj2021.client.game.graphics.*;
import com.topperbibb.hacktcnj2021.client.game.util.Triplet;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles initial configuration
 */
public class Config {

    /**
     * Reads information from config.json, if it exists, and parses out Sprites into the game for use in-game, as well as other configuration variables
     * This method also starts music from the config file, if any is specified
     * If any errors occur, or if any sprites are not specified, default sprites from defaulttiles.png are used
     */
    public static void readConfig() {
        ParsedJSON sprites;
        try {
            JSONTokener tokenizer = new JSONTokener(Engine.class.getResourceAsStream("/config.json"));
            JSONObject object = new JSONObject(tokenizer);
            sprites = new ParsedJSON();
            if (!object.isEmpty()) {
                sprites = ParsedJSON.parseJSON(object);
            }
            SpriteManager.spritesheet = new Spritesheet(sprites.spriteSheetPath);
            for (ParsedJSON.JsonSprite sprite : sprites.sprites) {
                SpriteManager.addSprite(new SpriteInfo(sprite.width, sprite.height, sprite.x, sprite.y, sprite.flipped, sprite.name, SpriteManager.spritesheet, sprite.pixelScale));
            }
            for (Triplet<String, List<ParsedJSON.JsonSprite>, Boolean> set : sprites.spriteSets) {
                List<SpriteInfo> infoList = set.getSecond().stream().map(jsonSprite -> new SpriteInfo(jsonSprite.width, jsonSprite.height, jsonSprite.x, jsonSprite.y, jsonSprite.flipped, jsonSprite.name, SpriteManager.spritesheet, jsonSprite.pixelScale)).collect(Collectors.toList());
                SpriteManager.addSprite(new SpriteSet(infoList, set.getFirst(), set.getThird()));
            }
            try {
                if (sprites.songPaths.size() > 0) {
                    AudioPlayer[] players = new AudioPlayer[sprites.songPaths.size()];
                    for (int i = 0; i < sprites.songPaths.size(); i++) {
                        String path = sprites.songPaths.get(i);
                        players[i] = AudioPlayer.getInstance(path);
                        players[i].setVolume(sprites.volume * 100);
                    }
                    if (sprites.shuffled) {
                        AudioPlayer.playShuffle(players);
                    } else {
                        AudioPlayer.playQueue(players);
                        players[players.length - 1].addFinishListener(new AudioPlayer.AudioFinishListener() {
                            @Override
                            public void onFinish() {
                                AudioPlayer.playQueue(players);
                            }
                        });
                    }
                } else {
                    AudioPlayer player = AudioPlayer.getInstance(sprites.songPath);
                    player.setVolume(sprites.volume * 100);
                    if (sprites.introPath != null) {
                        AudioPlayer introPlayer = AudioPlayer.getInstance(sprites.introPath);
                        introPlayer.setVolume(sprites.volume * 100);
                        introPlayer.playThen(() -> player.startLoop(Clip.LOOP_CONTINUOUSLY));
                    } else {
                        player.startLoop(Clip.LOOP_CONTINUOUSLY);
                    }
                }
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                e.printStackTrace();
            }
        } catch (NullPointerException | JSONException | ClassCastException e) {
            sprites = null;
        }

        Spritesheet defaultSpriteSheet = new Spritesheet("defaulttiles.png");

        SpriteManager.addSpriteDefault(new SpriteSet(Arrays.asList(new SpriteInfo(16, 16, 32, 88, "Player_right", defaultSpriteSheet), new SpriteInfo(16, 16, 48, 88, "Player_right", defaultSpriteSheet)), "Player_right"));
        SpriteManager.addSpriteDefault(new SpriteSet(Arrays.asList(new SpriteInfo(16, 16, 32, 88, FlipEnum.X,"Player_left", defaultSpriteSheet), new SpriteInfo(16, 16, 48, 88, FlipEnum.X,"Player_left", defaultSpriteSheet)), "Player_left"));
        SpriteManager.addSpriteDefault(new SpriteSet(Arrays.asList(new SpriteInfo(16, 16, 16, 88, "Player_up", defaultSpriteSheet), new SpriteInfo(16, 16, 16, 88, FlipEnum.X, "Player_up", defaultSpriteSheet)), "Player_up"));
        SpriteManager.addSpriteDefault(new SpriteSet(Arrays.asList(new SpriteInfo(16, 16, 0, 88, "Player_down", defaultSpriteSheet), new SpriteInfo(16, 16, 0, 88, FlipEnum.X,"Player_down", defaultSpriteSheet)), "Player_down"));

        SpriteManager.addSpriteDefault(new SpriteInfo(16, 16, 0, 0, "Basic_ground", defaultSpriteSheet));
        SpriteManager.addSpriteDefault(new SpriteInfo(16, 16, 128, 0, "Wall", defaultSpriteSheet));
        SpriteManager.addSpriteDefault(new SpriteInfo(16, 16, 96, 48, "End", defaultSpriteSheet));
        SpriteManager.addSpriteDefault(new SpriteInfo(16, 16, 80, 48, "Crate", defaultSpriteSheet));
        SpriteManager.addSpriteDefault(new SpriteInfo(8, 8, 56, 56, "Key", defaultSpriteSheet));

        SpriteManager.addSpriteDefault(new SpriteInfo(16, 16, 128, 24, "Spawn_point", defaultSpriteSheet));

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
