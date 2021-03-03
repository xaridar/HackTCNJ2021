package com.topperbibb.hacktcnj2021.client.game;

import com.topperbibb.hacktcnj2021.client.game.graphics.FlipEnum;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteManager;
import com.topperbibb.hacktcnj2021.client.game.util.Triplet;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a config.json file as a list of sprites and other config options
 */
public class SpriteJson {

    /**
     * Represents a singular sprite from a json
     */
    public static class JsonSprite {
        public String name;
        public int x;
        public int y;
        public FlipEnum flipped;
        public double pixelScale;
        public int width;
        public int height;

        /**
         * @param x the x pixel position of the sprite in the sprite sheet
         * @param y the x pixel position of the sprite in the sprite sheet
         * @param name the name of the sprite
         * @param flipped a FlipEnum telling how to flip a sprite
         * @param pixelScale the scale the display a sprite in in-game
         * @param height the pixel height of the sprite in the sprite sheet
         * @param width the pixel width of the sprite in the sprite sheet
         */
        public JsonSprite(int x, int y, String name, FlipEnum flipped, double pixelScale, int height, int width) {
            this.x = x;
            this.y = y;
            this.name = name;
            this.pixelScale = pixelScale;
            this.height = height;
            this.width = width;
            this.flipped = flipped;
        }
    }

    // The resource file path of the sprite sheet to use
    public String spriteSheetPath;


    // The default tile size for a sprite sheet (for a full tile)
    public int tileSize;

    // The default scale of the sprites; can be set per sprite
    public double pixelScale;

    // A list of sprites to configure
    public List<JsonSprite> sprites = new ArrayList<>();

    // A list of sprite sets for sprites; each contains a sprite name, a list of sprites, and a boolean telling whether it should be randomized
    public List<Triplet<String, List<JsonSprite>, Boolean>> spriteSets = new ArrayList<>();

    /**
     * Statically creates a SpriteJson object from a {@link JSONObject}
     * @param obj a JSONObject parsed from config.json
     * @return a new SpriteJson with the information from {@code obj}
     * @throws JSONException if a required value cannot be found
     * @throws ClassCastException if a value is of the wrong type
     */
    public static SpriteJson createSpriteJson(JSONObject obj) throws JSONException, ClassCastException {
        SpriteJson json = new SpriteJson();
        json.spriteSheetPath = obj.getString("spriteSheetPath");
        json.tileSize = obj.getInt("tileSize");
        if (obj.has("pixelScale"))
            json.pixelScale = obj.getDouble("pixelScale");
        else json.pixelScale = 4;
        Iterator<String> it = obj.getJSONObject("sprites").keys();
        while (it.hasNext()) {
            String key = it.next();
            JSONObject o = obj.getJSONObject("sprites").getJSONObject(key);
            if (key.equals("Basic_ground") && o.has("tiled")) SpriteManager.tileBorders = o.getBoolean("tiled");
            if (o.has("sprites")) {
                boolean randomized = false;
                boolean ordered;
                if (o.has("randomized")) {
                    randomized = o.getBoolean("randomized");
                }
                if (o.has("ordered")) {
                    ordered = o.getBoolean("ordered");
                    randomized = !ordered;
                }
                List<JsonSprite> sprites = new ArrayList<>();
                o.getJSONArray("sprites").forEach(sprite -> {
                    JSONObject spriteObj = ((JSONObject) sprite);
                    sprites.add(parseSprite(spriteObj, json.pixelScale, key));
                });
                json.spriteSets.add(Triplet.of(key, sprites, randomized));
            } else {
                json.sprites.add(parseSprite(o, json.pixelScale, key));
            }
        }
        return json;
    }

    private static JsonSprite parseSprite(JSONObject o, double pixelScale, String key) {
        int x = o.getInt("x");
        int y = o.getInt("y");
        int height;
        int width;
        try {
            height = o.getInt("height");
            width = o.getInt("width");
        } catch (JSONException e) {
            height = width = o.getInt("sideLength");
        }
        FlipEnum flipped = FlipEnum.NONE;
        if (o.has("flipped")) {
            flipped = FlipEnum.valueOf(o.getString("flipped").toUpperCase());
        }
        if (o.has("pixelScale")) {
            pixelScale = o.getDouble("pixelScale");
        }
        return new JsonSprite(x, y, key, flipped, pixelScale, height, width);
    }

}
