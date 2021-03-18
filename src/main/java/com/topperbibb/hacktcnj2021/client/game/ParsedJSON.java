package com.topperbibb.hacktcnj2021.client.game;

import com.topperbibb.hacktcnj2021.client.game.graphics.FlipEnum;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteManager;
import com.topperbibb.hacktcnj2021.client.game.util.Triplet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Represents a config.json file as a list of sprites and other config options
 */
public class ParsedJSON {

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
        public double prob;

        /**
         * @param x the x pixel position of the sprite in the sprite sheet
         * @param y the x pixel position of the sprite in the sprite sheet
         * @param name the name of the sprite
         * @param flipped a FlipEnum telling how to flip a sprite
         * @param pixelScale the scale the display a sprite in in-game
         * @param height the pixel height of the sprite in the sprite sheet
         * @param width the pixel width of the sprite in the sprite sheet
         * @param prob the probability of this sprite occurring in a random list
         */
        public JsonSprite(int x, int y, String name, FlipEnum flipped, double pixelScale, int height, int width, double prob) {
            this.x = x;
            this.y = y;
            this.name = name;
            this.pixelScale = pixelScale;
            this.height = height;
            this.width = width;
            this.flipped = flipped;
            this.prob = prob;
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

    // Variables to hold music information from the config.
    public String introPath;
    public String songPath;
    public List<String> songPaths = new ArrayList<>();
    public boolean shuffled;
    double volume = 1.00;

    // A map of String names to SFX
    public Map<String, String> sfx = new HashMap<>();

    /**
     * Statically creates a ParsedJSON object from a {@link JSONObject}
     * @param obj a JSONObject parsed from config.json
     * @return a new ParsedJSON with the information from {@code obj}
     * @throws JSONException if a required value cannot be found
     * @throws ClassCastException if a value is of the wrong type
     */
    public static ParsedJSON parseJSON(JSONObject obj) throws JSONException, ClassCastException {
        ParsedJSON json = new ParsedJSON();
        json.spriteSheetPath = obj.getString("spriteSheetPath");
        json.tileSize = obj.getInt("tileSize");
        if (obj.has("pixelScale"))
            json.pixelScale = obj.getDouble("pixelScale");
        else json.pixelScale = 4;
        Iterator<String> it = obj.getJSONObject("sprites").keys();
        if (obj.has("music")) {
            if (obj.get("music").getClass() == String.class) {
                json.songPath = obj.getString("music");
            } else {
                JSONObject musicObj = obj.getJSONObject("music");
                if (musicObj.has("intro")) json.introPath = musicObj.getString("intro");
                if (musicObj.get("songs").getClass() == String.class) json.songPath = musicObj.getString("songs");
                else {
                    boolean shuffled = false;
                    if (musicObj.has("shuffled")) {
                        shuffled = musicObj.getBoolean("shuffled");
                    }
                    JSONArray arr = musicObj.getJSONArray("songs");
                    arr.forEach(str -> json.songPaths.add((String) str));
                    json.shuffled = shuffled;
                }
            }
            if (obj.has("volume")) {
                json.volume = obj.getDouble("volume");
            }
        }
        if (obj.has("sfx")) {
            obj.getJSONObject("sfx").keys().forEachRemaining(key -> {
                json.sfx.put(key, obj.getJSONObject("sfx").getString(key));
            });
        }
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
        double prob = 1;
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
        if (o.has("probability")) {
            prob = o.getDouble("probability");
        }
        return new JsonSprite(x, y, key, flipped, pixelScale, height, width, prob);
    }

}
